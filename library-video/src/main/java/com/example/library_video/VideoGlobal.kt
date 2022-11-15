package com.example.library_video

import android.app.Activity
import android.widget.ImageView
import com.example.librery_base.extension.load
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 19
 */
fun startAutoPlay(
    activity: Activity,
    player: GSYVideoPlayer,
    position: Int,
    playUrl: String,
    coverUrl: String,
    playTag: String,
    callBack: GSYSampleCallBack? = null
) {
    player.run {
        //防止错位设置
        setPlayTag(playTag)
        //设置播放位置防止错位
        setPlayPosition(position)
        //音频焦点冲突时是否释放
        setReleaseWhenLossAudio(false)
        //设置循环播放
        setLooping(true)
        //增加封面
        val cover = ImageView(activity)
        cover.scaleType = ImageView.ScaleType.CENTER_CROP
        cover.load(coverUrl, 4f)
        cover.parent?.run { removeView(cover) }
        setThumbImageView(cover)
        //设置播放过程中的回调
        setVideoAllCallBack(callBack)
        //设置播放URL
        setUp(playUrl, false, null)
    }
}