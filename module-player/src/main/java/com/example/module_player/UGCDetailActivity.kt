package com.example.module_player

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_common.databinding.ItemFooterTextcard3Binding
import com.example.library_common.utils.IntentDataHolderUtils
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.activity.MvvmBaseActivity
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_player.adapter.UGCDetailAdapter
import com.example.module_player.databinding.ActivityUgcDetailBinding
import com.gyf.immersionbar.ImmersionBar
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.CommonUtil
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

/**
 *社区-推荐详情
 */
@Route(path = RouterActivityPath.Ugc.PAGER_USCDETAIL)
class UGCDetailActivity : MvvmBaseActivity<ActivityUgcDetailBinding, UGCDetailViewModel>() {

    private lateinit var mAdapter: UGCDetailAdapter

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        onPageChangeCallback?.run { viewDataBinding.viewPager2.unregisterOnPageChangeCallback(this) }
        onPageChangeCallback = null

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.ani_push_bottom_out)
    }

    override fun initView() {
        if (chechArugments()) {
            ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f)
                .statusBarColor(R.color.black).fitsSystemWindows(true).init()
            //第一次进入
            if (vm?.dataList == null) {
                //获取当前位置
                vm?.itemPosition = getCurrentItemPosition()
                //将数据注入ViewModel
                vm?.dataList = IntentDataHolderUtils.getData<List<CommunityCommedBean.Item>>(
                    EXTRA_RECOMMEND_ITEM_LIST_JSON
                )
            }
            if (vm?.dataList == null) {
                //说明未携带数据
                ToastUtils.show(GlobalUtils.getString(R.string.jump_page_unknown_error))
                finish()
            } else {
                //说明数据注入成功
                mAdapter = UGCDetailAdapter(this, vm?.dataList!!)
                viewDataBinding.viewPager2.adapter = mAdapter
                viewDataBinding.viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
                viewDataBinding.viewPager2.offscreenPageLimit = 1
                onPageChangeCallback = AutoPlayPageChangeListener(
                    viewDataBinding.viewPager2,
                    getCurrentItemPosition(),
                    R.id.detail_videoplayer
                )
                viewDataBinding.viewPager2.registerOnPageChangeCallback(onPageChangeCallback!!)
                viewDataBinding.viewPager2.setCurrentItem(vm?.itemPosition!!, false)
            }
        }
    }

    private fun getCurrentItemPosition(): Int {
        val list = IntentDataHolderUtils.getData<List<CommunityCommedBean.Item>>(
            EXTRA_RECOMMEND_ITEM_LIST_JSON
        )
        val currentItem = IntentDataHolderUtils.getData<CommunityCommedBean.Item>(
            EXTRA_RECOMMEND_ITEM_JSON
        )
        list?.forEachIndexed { index, item ->
            if (currentItem == item) {
                vm?.itemPosition = index
                return@forEachIndexed
            }
        }
        return vm?.itemPosition!!
    }

    private fun chechArugments(): Boolean {
        return if (IntentDataHolderUtils.getData<CommunityCommedBean.Item>(
                EXTRA_RECOMMEND_ITEM_LIST_JSON
            ) == null ||
            IntentDataHolderUtils.getData<CommunityCommedBean.Item>(EXTRA_RECOMMEND_ITEM_JSON) == null
        ) {
            ToastUtils.show(GlobalUtils.getString(R.string.jump_page_unknown_error))
            finish()
            false
        } else {
            true
        }
    }

    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    override fun getViewModel(): UGCDetailViewModel {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UGCDetailViewModel::class.java
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_ugc_detail
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun onRetryClick() {

    }

    override fun startLoading() {

    }

    override fun loadFinish() {

    }

    override fun loadFailed(message: String) {

    }

    companion object {
        const val TAG = "UgcDetailActivity"
        const val EXTRA_RECOMMEND_ITEM_LIST_JSON = "recommend_item_list"
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommend_item"
    }

    class AutoPlayPageChangeListener(
        private val viewPager2: ViewPager2,
        private var defaultPosition: Int,
        private val itemPlayId: Int
    ) : ViewPager2.OnPageChangeCallback() {
        //防止当前页面抖动，触发OnPageScrollStateChange回调
        private var isPageSelected = false
        private var isNeedShowWifiDialog = true

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            Log.d(
                TAG,
                "onPageSelected>>>position:${position},positionOffsetPixels:${positionOffsetPixels}"
            )
            if (defaultPosition == position && positionOffsetPixels == 0) {
                //移到默认位置
                //进入页面后，调用setCurrentItem函数，手动触发onPageScrollStateChanged函数。
                onPageScrollStateChanged(ViewPager2.SCROLL_STATE_IDLE)
                defaultPosition = -1
            }
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Log.d(TAG, "onPageSelected>>>position:${position}")
            isPageSelected = true
        }


        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            Log.d(TAG, "onPageScrollStateChanged>>>state:${state}")
            if (state == ViewPager2.SCROLL_STATE_IDLE && isPageSelected) {
                //说明此页面被选中,播放视频
                playVideo()
                isPageSelected = false
            }
        }

        private fun playVideo() {
            var gsyVideoPlayer: GSYBaseVideoPlayer? = null
            var needPlay = false
            for (childIndex in 0 until viewPager2.childCount) {
                //ViewPager2内部使用的RecyclerView作为childview
                val view = viewPager2.getChildAt(childIndex)
                if (view != null && view is RecyclerView) {
                    val layoutManager = view.layoutManager
                    val childCount =
                        view.childCount//返回offscreenPageLimit(两侧保留的屏幕页数) * 2 + 1（当前页数）。offscreenPageLimit 设置1，则返回值为3
                    for (index in 0 until childCount) {
                        if (layoutManager?.getChildAt(index) != null && layoutManager.getChildAt(
                                index
                            )?.findViewById<View>(itemPlayId) != null
                        ) {
                            val player = layoutManager.getChildAt(index)
                                ?.findViewById<View>(itemPlayId) as GSYBaseVideoPlayer
                            val rect = Rect()
                            player.getLocalVisibleRect(rect)
                            val height = player.height
                            val isPlayerVisible = rect.top == 0 && rect.bottom == height//在屏幕可见区域
                            if (isPlayerVisible && player.visibility == View.VISIBLE && player.currentPlayer.currentState == GSYVideoPlayer.CURRENT_STATE_NORMAL || player.currentPlayer.currentState == GSYVideoPlayer.CURRENT_STATE_ERROR) {
                                gsyVideoPlayer = player
                                //播放器可见就播放
                                needPlay=true
                            } else {
                                GSYVideoManager.releaseAllVideos()
                            }
                        }
                    }

                }

            }
            if (gsyVideoPlayer != null && needPlay) {
                startPlayLogic(gsyVideoPlayer, gsyVideoPlayer.context)
            }
        }

        private fun startPlayLogic(gsyVideoPlayer: GSYBaseVideoPlayer, context: Context) {
            if (!CommonUtil.isWifiConnected(context) && isNeedShowWifiDialog) {
                showWifiDialog(gsyVideoPlayer, context)
                return
            }
            //连接了wifi，直接播放
            gsyVideoPlayer.startPlayLogic()
        }

        private fun showWifiDialog(gsyVideoPlayer: GSYBaseVideoPlayer, context: Context) {
            if (!NetworkUtils.isAvailable(context)) {
                //没有连接网络
                ToastUtils.show(context.resources.getString(R.string.no_net))
                return
            }
            AlertDialog.Builder(context).apply {
                setMessage(context.resources.getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi))
                setPositiveButton(R.string.tips_not_wifi_confirm) { dialog, which ->
                    dialog.dismiss()
                    gsyVideoPlayer.startPlayLogic()
                    isNeedShowWifiDialog = false
                }
                setNegativeButton(R.string.tips_not_wifi_cancel) { dialog, which ->
                    dialog.dismiss()
                    isNeedShowWifiDialog = true
                }
                    .create()
            }.show()
        }

        companion object {
            const val TAG = "AutoPlayChangeListener"
        }
    }

}



