package com.example.library_common.viewmodel

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_network.GlobalNetwork
import com.example.library_network.VideoRepository
import com.example.library_network.api.VideoService
import com.example.library_network.bean.*
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import kotlinx.parcelize.Parcelize
import java.lang.Exception

/**
 *description : <p>
 *视频详情页面的ViewModel
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 22
 */
class NewDetailViewModel(private val globalNetwork: GlobalNetwork) : ViewModel(),
    IMvvmBaseVM<IBaseView> {
    //推荐视频
    var relatedDataList = ArrayList<VideoRelated.Item>()

    //评论
    var repliesDataList = ArrayList<VideoReplies.Item>()

    //需要的视频信息
    var videoInfoData: VideoInfo? = null

    var videoId = 0L

    private var videoRepliesRequestLiveData = MutableLiveData<String>()

    private var videoDetailRequestLiveData = MutableLiveData<RequestParam>()

    private var videoRelatedAndRepliesRequestLiveData = MutableLiveData<RequestParam>()

    var nextPageUrl: String? = null

    val videoDetailLiveData = Transformations.switchMap(videoDetailRequestLiveData) {
        liveData {
            val result = try {
                val videoDetail = VideoRepository.getInstance(globalNetwork)
                    .refreshVideoDetail(it.videoId, it.repliesUrl)
                Result.success(videoDetail)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }

    val videoRelatedAndRepliesLiveData =
        Transformations.switchMap(videoRelatedAndRepliesRequestLiveData) {
            liveData {
                val result = try {
                    val videoRelatedAndReplies = VideoRepository.getInstance(globalNetwork)
                        .refreshVideoRelatedAndReplies(it.videoId, it.repliesUrl)
                    Result.success(videoRelatedAndReplies)
                } catch (e: Exception) {
                    Result.failure(e)
                }
                emit(result)
            }
        }

    val videoRepliesLiveData = Transformations.switchMap(videoRepliesRequestLiveData) {
        liveData {
            val result = try {
                val videoReplies =
                    VideoRepository.getInstance(globalNetwork).refreshVideoReplies(it)
                Result.success(videoReplies)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }

    //下拉刷新全刷新
    fun onRefresh() {
        if (videoInfoData == null) {
            videoDetailRequestLiveData.value =
                RequestParam(videoId, "${VideoService.VIDEO_REPLIES_URL}$videoId")
        } else {
            videoRelatedAndRepliesRequestLiveData.value = RequestParam(
                videoInfoData?.videoId ?: 0L,
                "${VideoService.VIDEO_REPLIES_URL}${videoInfoData?.videoId ?: 0L}"
            )
        }
    }

    //上拉加载只用加载更多评论
    fun onLoadMore() {
        videoRepliesRequestLiveData.value = nextPageUrl ?: ""
    }


    override fun attachUi(view: IBaseView) {

    }

    override fun getView(): IBaseView? {
        return null
    }

    override fun isAttachUi(): Boolean {
        return false
    }

    override fun detachUi() {

    }

    inner class RequestParam(val videoId: Long, val repliesUrl: String)

    @Parcelize
    data class VideoInfo(
        val videoId: Long,
        val playUrl: String,
        val title: String,
        val description: String,
        val category: String,
        val library: String,
        val consumption: Consumption,
        val cover: Cover,
        val author: Author?,
        val webUrl: WebUrl
    ) : Parcelable
}