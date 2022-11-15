package com.example.library_video

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

/**
 *description : <p>
 *社区推荐页面对应的视频播放器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/26 22
 */
class UgcDetailVideoPlayer : StandardGSYVideoPlayer {
    //是否第一次加载视频用域隐藏进度条、播放按钮等ui，播放完成后重新加载视频，会重置为true
    private var initFirstLoad = true

    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    override fun getLayoutId(): Int = R.layout.ugc_video_detail

    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> imageView.setImageResource(R.drawable.ic_pause_white_24dp)
                GSYVideoView.CURRENT_STATE_ERROR -> imageView.setImageResource(R.drawable.ic_play_white_24dp)
                GSYVideoView.CURRENT_STATE_AUTO_COMPLETE -> imageView.setImageResource(R.drawable.ic_refresh_white_24dp)
                else -> imageView.setImageResource(R.drawable.ic_play_white_24dp)
            }
        } else {
            super.updateStartImage()
        }
    }

    //正常
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        Log.d(javaClass.simpleName, "changeUiToNormal: ")
        initFirstLoad = true
    }

    //准备中
    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        Log.d(javaClass.simpleName, "changeUiToPreparingShow: ")
        mBottomContainer.visibility = View.GONE
    }

    //播放中
    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingShow: ")
        if (initFirstLoad) mBottomContainer.visibility = View.GONE

        initFirstLoad = false
    }

    //开始缓冲
    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingBufferingShow: ")
        if (initFirstLoad) {
            mStartButton.visibility = View.GONE
            initFirstLoad = false
        } else {
            mStartButton.visibility = View.VISIBLE
        }
    }

    //暂停
    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        Log.d(javaClass.simpleName, "changeUiToPauseShow: ")
    }

    //自动播放结束
    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        Log.d(javaClass.simpleName, "changeUiToCompleteShow: ")
        mBottomContainer.visibility = View.GONE
    }

    //错误
    override fun changeUiToError() {
        super.changeUiToError()
        Log.d(javaClass.simpleName, "changeUiToError: ")

    }

    fun getBottomContaniner() = mBottomContainer
}