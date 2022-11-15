package com.example.module_community.attention

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.librery_base.global.dp2px
import com.example.librery_base.global.screenHeight
import com.shuyu.gsyvideoplayer.R
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 20
 */
class AutoPlayScrollListener(private val itemPlayId:Int,private val rangeTop: Int, private val rangeBottom: Int) : RecyclerView.OnScrollListener() {
    //需要显示网络对话框
    private var isNeedShowWifiDialog=true
    //第一次可见
    private var firstVisible=0
    //最后一次可见
    private var lastVisible=0
    //可见的item个数
    private var visibleCount=0

    private var runnable:PlayRunnable?=null


    private var playhander=Handler()
    //滚动时
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibleItem=(recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItem=(recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (firstVisibleItem==lastVisibleItem)return
        firstVisible=firstVisibleItem
        lastVisible=lastVisibleItem
        visibleCount=lastVisibleItem-firstVisibleItem
    }

    /**
     * 滚动状态改变
     * @param recyclerView RecyclerView
     * @param newState Int
     */
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when(newState){
            //滑动结束后，回到IDLE的状态
            RecyclerView.SCROLL_STATE_IDLE->playVideo(recyclerView)
        }
    }

    private fun playVideo(recyclerView: RecyclerView) {
        recyclerView.run {
            val layoutManager=recyclerView.layoutManager
            var gsyBaseVideoPlayer:GSYBaseVideoPlayer?=null
            var needPlay=false
            for (i in 0 until visibleCount){
                //获取屏幕可见的itemview
                if (layoutManager?.getChildAt(i)!=null&&layoutManager.getChildAt(i)?.findViewById<View>(itemPlayId)!=null){
                    //获取可见的VideoPlayer
                    val player=layoutManager.getChildAt(i)?.findViewById<View>(itemPlayId) as GSYBaseVideoPlayer
                    val rect= Rect()
                    //获取view的可见区域，相对于屏幕的坐标
                    player.getLocalVisibleRect(rect)
                    val height=player.height
                    if (rect.top==0&&rect.bottom==height){
                        //说明是第一个可见
                        gsyBaseVideoPlayer=player
                        if (player.currentPlayer.currentState==GSYBaseVideoPlayer.CURRENT_STATE_NORMAL||
                                player.currentPlayer.currentState==GSYBaseVideoPlayer.CURRENT_STATE_ERROR){
                            needPlay=true
                        }
                        break
                    }
                }
            }
            if (gsyBaseVideoPlayer!=null&&needPlay){
                runnable?.let {
                    val tmpPlayer=it.gsyVideoPlayer
                    playhander.removeCallbacks(it)
                    runnable=null
                    if (tmpPlayer===gsyBaseVideoPlayer)return
                }
                runnable=PlayRunnable(gsyBaseVideoPlayer)
                //降低频率
                playhander.postDelayed(runnable!!,400)
            }
        }
    }

    private inner class PlayRunnable(var gsyVideoPlayer:GSYBaseVideoPlayer?):Runnable {
        override fun run() {
            //是否在区域内
            var inPosition=false
            //如果未播放，需要播放
            if (gsyVideoPlayer!=null){
                val screenPosition=IntArray(2)
                //获取view在屏幕的坐标
                gsyVideoPlayer!!.getLocationOnScreen(screenPosition)
                //视频播放器的一半
                val halfHeight=gsyVideoPlayer!!.height/2
                //屏幕中间的位置
                val rangePosition=screenPosition[1]+halfHeight
                //中心点在播放区域内
                if (rangePosition>=rangeTop&&rangePosition<=rangeBottom){
                    inPosition=true
                }
                if (inPosition){
                    //在播放区域内就开始播放
                    startPlayLogic(gsyVideoPlayer!!,gsyVideoPlayer!!.context)
                }
            }
        }

    }

    private fun startPlayLogic(gsyVideoPlayer: GSYBaseVideoPlayer, context: Context) {
        if (!CommonUtil.isWifiConnected(context)&&isNeedShowWifiDialog){
            //没有连接wifi需要显示对话框
            showWifiDialog(gsyVideoPlayer,context)
            return
        }
        gsyVideoPlayer.startPlayLogic()
    }

    private fun showWifiDialog(gsyVideoPlayer: GSYBaseVideoPlayer, context: Context) {
        if (!NetworkUtils.isAvailable(context)) {
            Toast.makeText(context, context.resources.getString(R.string.no_net), Toast.LENGTH_LONG).show()
            return
        }
        AlertDialog.Builder(context).apply {
            setMessage(context.resources.getString(R.string.tips_not_wifi))
            setPositiveButton(context.resources.getString(R.string.tips_not_wifi_confirm)) { dialog, which ->
                dialog.dismiss()
                gsyVideoPlayer.startPlayLogic()
                isNeedShowWifiDialog = false
            }
            setNegativeButton(context.resources.getString(R.string.tips_not_wifi_cancel)) { dialog, which ->
                dialog.dismiss()
                isNeedShowWifiDialog = true
            }
            create()
        }.show()
    }

    companion object {
        /**
         *指定自动播放，在屏幕的区域范围。上半部分
         */
        val PLAY_RANGE_TOP = screenHeight / 2 - dp2px(180F)

        /**
         *指定自动播放，在屏幕的区域范围。下半部分
         */
        val PLAY_RANGE_BOTTOM = screenHeight / 2 + dp2px(180F)
    }
}