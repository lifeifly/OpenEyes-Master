package com.example.library_network

import com.example.library_network.bean.VideoDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 *description : <p>
 *视频数据的管理类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 23
 */
class VideoRepository private constructor(private val globalNetwork: GlobalNetwork) {
    //刷新评论
    suspend fun refreshVideoReplies(repliesUrl: String) = requestVideoReplies(repliesUrl)

    //刷新视频推荐和评论
    suspend fun refreshVideoRelatedAndReplies(videoId: Long, repliesUrl: String) =
        requestVideoRelatedAndReplies(videoId, repliesUrl)

    suspend fun refreshVideoDetail(videoId: Long, repliesUrl: String) =
        requestVideoDetail(videoId, repliesUrl)


    private suspend fun requestVideoReplies(repliesUrl: String) = withContext(Dispatchers.IO) {
        //切换到IO执行任务完成后在切回原线程
        coroutineScope {
            val deferredVideoReplies = async { globalNetwork.fetchVideoReplies(repliesUrl) }
            val videoReplies = deferredVideoReplies.await()
            videoReplies
        }
    }

    private suspend fun requestVideoRelatedAndReplies(videoId: Long, repliesUrl: String) =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val defferredVideoRelated = async { globalNetwork.fetchVideoRecommend(videoId) }
                val defferredVideoReplies = async { globalNetwork.fetchVideoReplies(repliesUrl) }
                val videoRelated = defferredVideoRelated.await()
                val videoReplies = defferredVideoReplies.await()
                val videoDetail = VideoDetail(null, videoRelated, videoReplies)
                videoDetail
            }
        }

    private suspend fun requestVideoDetail(videoId: Long, repliesUrl: String) =
        withContext(Dispatchers.IO) {
            val deferredVideoInfo=async { globalNetwork.fetchVideoInfo(videoId) }
            val deferredVideoRelated=async { globalNetwork.fetchVideoRecommend(videoId) }
            val deferredVideoReplies=async { globalNetwork.fetchVideoReplies(repliesUrl) }
            val videoInfo=deferredVideoInfo.await()
            val videoRelated=deferredVideoRelated.await()
            val videoReplies=deferredVideoReplies.await()

            val videoDetail=VideoDetail(videoInfo,videoRelated, videoReplies)
            videoDetail
        }

    companion object{
        private var videoRepository:VideoRepository?=null

        fun getInstance(globalNetwork: GlobalNetwork):VideoRepository{
            if (videoRepository==null){
                synchronized(VideoRepository::class.java){
                    if (videoRepository==null){
                        videoRepository= VideoRepository(globalNetwork)
                    }
                }
            }
            return videoRepository!!
        }
    }
}