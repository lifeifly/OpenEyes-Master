package com.example.library_video

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

/**
 *description : <p>
 *视频播放器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/26 16
 */
class AutoPlayerViewPlayer : StandardGSYVideoPlayer {

    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId(): Int =R.layout.auto_play_video_player

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false
    }

    /**
     * 定义开始按键显示
     */
    override fun updateStartImage() {
        super.updateStartImage()
        if (mStartButton is ImageView){
            val imageView=mStartButton as ImageView
            when(mCurrentState){
                GSYVideoView.CURRENT_STATE_PLAYING->imageView.setImageResource(R.drawable.exo_icon_pause)
                GSYVideoView.CURRENT_STATE_ERROR->imageView.setImageResource(R.drawable.exo_icon_play)
                else->imageView.setImageResource(R.drawable.exo_icon_play)
            }
        }else{
            super.updateStartImage()
        }
    }

    override fun touchDoubleUp(e: MotionEvent?) {
        super.touchDoubleUp(e)
        //不需要双击暂停
    }
    //正常状态
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        Log.d(javaClass.simpleName, "changeUiToNormal: ")
        mBottomContainer.visibility= View.GONE
    }
    //准备中
    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        Log.d(javaClass.simpleName, "changeUiToPreparingShow: ")
        mBottomContainer.visibility=View.GONE
    }

    //播放中
    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingShow: ")
        mBottomContainer.visibility=View.GONE
        mStartButton.visibility=View.GONE
    }
    //开始缓冲
    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        Log.d(javaClass.simpleName, "changeUiToPlayingBufferingShow: ")
    }
    //暂停
    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        mBottomContainer.visibility=View.GONE
        Log.d(javaClass.simpleName, "changeUiToPauseShow: ")
    }
    //自动播放结束
    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        Log.d(javaClass.simpleName, "changeUiToCompleteShow: ")
    }
    //错误状态
    override fun changeUiToError() {
        super.changeUiToError()
        Log.d(javaClass.simpleName, "changeUiToError: ")
    }
}