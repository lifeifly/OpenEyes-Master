package com.example.library_video

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.librery_base.extension.gone
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

/**
 *description : <p>
 *视频详情页面的视频播放器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 20
 */
class DetailVideoPlayer : StandardGSYVideoPlayer {
    //是否第一次加载用于隐藏进度条、播放按钮等UI。播放完成后，重新加载视频，会重置为true。
    private var intiFirstLoad = true

    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    //自定义布局
    override fun getLayoutId() = R.layout.detail_video_player

    //根据播放器的状态修改start图标的状态
    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> {
                    //播放时将图标换为暂停
                    imageView.setImageResource(R.drawable.ic_pause_white_24dp)
                    //并将图标设置为正常时不可见，点击可见
                    imageView.setBackgroundResource(R.drawable.sel_pause_white_bg)
                }
                GSYVideoView.CURRENT_STATE_ERROR -> {
                    //出错时将图标换为播放
                    imageView.setImageResource(R.drawable.ic_play_white_24dp)
                    //并将图标设置为正常时可见，点击不可见
                    imageView.setBackgroundResource(R.drawable.sel_play_white_bg)
                }
                GSYVideoView.CURRENT_STATE_AUTO_COMPLETE -> {
                    //自动播放结束时将图标换为刷新图标
                    imageView.setImageResource(R.drawable.ic_refresh_white_24dp)
                    imageView.setBackgroundResource(0)
                }
                else -> {
                    //其他状态都显示播放按钮
                    imageView.setImageResource(R.drawable.ic_play_white_24dp)
                    //并将图标设置为正常时不可见，点击可见
                    imageView.setBackgroundResource(R.drawable.sel_play_white_bg)
                }
            }
        }else{
            super.updateStartImage()
        }
    }
    //正常
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        Log.d(javaClass.simpleName, "changeUiToNormal")
        intiFirstLoad = true
    }

    //准备中
    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        Log.d(javaClass.simpleName, "changeUiToPreparingShow")
        mBottomContainer.gone()
        mStartButton.gone()
    }
    //开始缓冲
    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingBufferingShow")
    }
    //播放中
    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingShow")
        if (intiFirstLoad) {
            mBottomContainer.gone()
            mStartButton.gone()
        }
        intiFirstLoad = false
    }



    //暂停
    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        Log.d(javaClass.simpleName, "changeUiToPauseShow")
    }

    //自动播放结束
    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        Log.d(javaClass.simpleName, "changeUiToCompleteShow")
        mBottomContainer.gone()
    }

    //错误状态
    override fun changeUiToError() {
        super.changeUiToError()
        Log.d(javaClass.simpleName, "changeUiToError")
    }

    fun getBottomContainer() = mBottomContainer


}