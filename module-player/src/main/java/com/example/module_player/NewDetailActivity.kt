package com.example.module_player

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const.ExtraTag.Companion.EXTRA_VIDEOINFO
import com.example.library_common.Const.ExtraTag.Companion.EXTRA_VIDEO_ID
import com.example.library_common.global.InjectViewModel
import com.example.library_common.recyclerview.NoStatusFooter
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.librery_base.activity.MvvmBaseActivity
import com.example.librery_base.extension.*
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.*
import com.example.module_player.adapter.NewDetailRelatedAdapter
import com.example.module_player.adapter.NewDetailRepliesAdapter
import com.example.module_player.databinding.ActivityNewDetailBinding
import com.gyf.immersionbar.ImmersionBar
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.GSYADVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.coroutines.*

@Route(path = RouterActivityPath.Detail.PAGER_DETAIL)
class NewDetailActivity : MvvmBaseActivity<ActivityNewDetailBinding, NewDetailViewModel>() {

    private lateinit var relatedAdapter: NewDetailRelatedAdapter
    private lateinit var repliesAdapter: NewDetailRepliesAdapter

    //合并的适配器
    private lateinit var concatAdapter: ConcatAdapter

    //处理屏幕旋转的逻辑工具类
    private var orientaionUtils: OrientationUtils? = null

    private val globalJob by lazy { Job() }

    private var hideTitleBarJob: Job? = null

    private var hideBottomContainerJob: Job? = null

    override fun getViewModel(): NewDetailViewModel {
        return ViewModelProvider(
            this,
            InjectViewModel.getNewDetailViewModelFactory()
        ).get(NewDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkArguments()) {
            ImmersionBar.with(this)
                .autoStatusBarDarkModeEnable(true, 0.2f)
                .statusBarColor(R.color.black)
                .fitsSystemWindows(true)
                .init()
        }

    }

    override fun initView() {
        initParams()
        orientaionUtils = OrientationUtils(this, viewDataBinding.videoPlayer)
        relatedAdapter = NewDetailRelatedAdapter(this, vm?.relatedDataList!!, vm?.videoInfoData)
        repliesAdapter = NewDetailRepliesAdapter(this, vm?.repliesDataList!!)
        concatAdapter = ConcatAdapter(relatedAdapter, repliesAdapter)
        viewDataBinding.recyclerview.layoutManager = LinearLayoutManager(this)
        viewDataBinding.recyclerview.adapter = concatAdapter
        viewDataBinding.recyclerview.setHasFixedSize(true)
        viewDataBinding.recyclerview.itemAnimator = null
        viewDataBinding.refreshLayout.run {
            setDragRate(0.7F)
            setHeaderTriggerRate(0.6F)
            setFooterTriggerRate(0.6F)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            viewDataBinding.refreshLayout.setEnableNestedScroll(true)
            setFooterHeight(153F)
            setRefreshFooter(NoStatusFooter(this@NewDetailActivity).apply {
                setAccentColorId(R.color.white)
                setTextTitleSize(16F)
            })
            setOnRefreshListener {
                //下拉刷新时不去刷新而是退出
                finish()
            }
            setOnLoadMoreListener {
                vm?.onLoadMore()
            }
        }
        viewDataBinding.apply {
            setOnClickListener(
                ivPullDown,
                ivMore,
                ivShare,
                ivCollect,
                ivToWechatFriends,
                ivToWechatMemories,
                ivToWeibo,
                ivToQQ,
                ivToQQZone,
                ivAvatarComment,
                etComment,
                ivComment,
                tvComment,
                listener = ClickListener()
            )
        }
        observe()
        startVideoPlayer()
        vm?.onRefresh()
    }

    private fun observe() {
        //刷新 视频信息+相关推荐+评论
        if (!vm?.videoDetailLiveData?.hasObservers()!!) {
            vm?.videoDetailLiveData?.observe(this, Observer {
                val response = it.getOrNull()
                if (response == null) {
                    ToastUtils.show(ResponseHandler.getFailureTips(it.exceptionOrNull()))
                    return@Observer
                }
                vm?.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null || response.videoRelated?.itemList.isNullOrEmpty()
                    && response.videoReplies.itemList.isNullOrEmpty()
                ) {
                    return@Observer
                }
                response.videoBeanForClient?.run {
                    vm?.videoInfoData = NewDetailViewModel.VideoInfo(
                        id,
                        playUrl,
                        title,
                        description,
                        category,
                        library,
                        consumption,
                        cover,
                        author,
                        webUrl
                    )
                    startVideoPlayer()
                    relatedAdapter.bindVideoInfo(vm?.videoInfoData!!)
                }
                vm?.relatedDataList?.clear()
                vm?.repliesDataList?.clear()
                vm?.relatedDataList?.addAll(response.videoRelated?.itemList!!)
                vm?.repliesDataList?.addAll(response.videoReplies.itemList)
                relatedAdapter.notifyDataSetChanged()
                repliesAdapter.notifyDataSetChanged()

                when {
                    vm?.relatedDataList.isNullOrEmpty() -> viewDataBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> viewDataBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> viewDataBinding.refreshLayout.closeHeaderOrFooter()
                }
            })
        }
        //刷新 相关推荐+评论
        if (!vm?.videoRelatedAndRepliesLiveData?.hasObservers()!!) {
            vm?.videoRelatedAndRepliesLiveData?.observe(this, Observer {
                val response = it.getOrNull()
                if (response == null) {
                    ToastUtils.show(ResponseHandler.getFailureTips(it.exceptionOrNull()))
                    return@Observer
                }
                vm?.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null || response.videoRelated?.itemList.isNullOrEmpty() && response.videoReplies.itemList.isNullOrEmpty()) {
                    return@Observer
                }
                vm?.relatedDataList?.clear()
                vm?.repliesDataList?.clear()
                vm?.relatedDataList?.addAll(response.videoRelated?.itemList!!)
                vm?.repliesDataList?.addAll(response.videoReplies.itemList)
                relatedAdapter.notifyDataSetChanged()
                repliesAdapter.notifyDataSetChanged()

                when {
                    vm?.relatedDataList.isNullOrEmpty() -> viewDataBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> viewDataBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> viewDataBinding.refreshLayout.closeHeaderOrFooter()
                }
            })
        }
        //上拉加载评论
        if (!vm?.videoRepliesLiveData?.hasObservers()!!) {
            vm?.videoRepliesLiveData?.observe(this, Observer {
                val response = it.getOrNull()
                if (response == null) {
                    ToastUtils.show(ResponseHandler.getFailureTips(it.exceptionOrNull()))
                    return@Observer
                }
                vm?.nextPageUrl = response.nextPageUrl
                if (response.itemList.isNullOrEmpty()) {
                    return@Observer
                }
                val itemCount = relatedAdapter.itemCount
                vm?.repliesDataList?.addAll(response.itemList)
                repliesAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)
                if (response.nextPageUrl.isNullOrEmpty()) {
                    viewDataBinding.refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    viewDataBinding.refreshLayout.closeHeaderOrFooter()
                }
            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (checkArguments()) {
            initParams()
            startVideoPlayer()
            vm?.onRefresh()
        }

    }

    override fun onPause() {
        super.onPause()
        viewDataBinding.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoADManager.releaseAllVideos()
        orientaionUtils?.releaseListener()
        viewDataBinding.videoPlayer.release()
        viewDataBinding.videoPlayer.setVideoAllCallBack(null)
        globalJob.cancel()
    }

    override fun onBackPressed() {
        orientaionUtils?.backToProtVideo()
        if (GSYVideoADManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.ani_push_bottom_out)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewDataBinding.videoPlayer.onConfigurationChanged(
            this,
            newConfig,
            orientaionUtils,
            true,
            true
        )
    }


    private fun startVideoPlayer() {
        vm?.videoInfoData?.run {
            viewDataBinding.ivBlurredBg.load(cover.blurred)
            viewDataBinding.tvComment.text = consumption.replyCount.toString()
            viewDataBinding.videoPlayer.startPlay()
        }
    }

    private fun GSYVideoPlayer.startPlay() {
        vm?.videoInfoData?.let {
            //设置全屏按键功能使用的是选择屏幕而不是全屏
            fullscreenButton.setOnClickListener { showFullScreen() }
            //防止错位设置
            playTag = TAG
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //增加封面
            val imageView = ImageView(this@NewDetailActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.load(it.cover.detail)
            thumbImageView = imageView
            thumbImageView.setOnClickListener { switchTitleBarVisible() }
            //是否开启自动旋转
            isRotateViewAuto = false
            //是否需要全屏锁定屏幕功能
            isNeedLockFull = true
            //是否可以滑动界面改变进度，声音等
            setIsTouchWiget(true)
            //设置触摸显示控制条的消失事件
            dismissControlTime = 5000
            //设置播放中的回调
            setVideoAllCallBack(VideoCallPlayBack())
            //设置播放的URL
            setUp(it.playUrl, false, it.title)
            //开始播放
            startPlayLogic()
        }
    }

    private fun switchTitleBarVisible() {
        if (viewDataBinding.videoPlayer.currentPlayer.currentState == GSYADVideoPlayer.CURRENT_STATE_AUTO_COMPLETE) return
        if (viewDataBinding.headerControll.visibility == View.VISIBLE) {
            Log.d(TAG, "switchTitleBarVisible:0 ")
            hideTitleBar()
        } else {
            Log.d(TAG, "switchTitleBarVisible:1 ")
            viewDataBinding.headerControll.visibleAlphaAnimation(1000)
            viewDataBinding.ivPullDown.visibleAlphaAnimation(1000)
            viewDataBinding.ivCollect.visibleAlphaAnimation(1000)
            viewDataBinding.ivMore.visibleAlphaAnimation(1000)
            viewDataBinding.ivShare.visibleAlphaAnimation(1000)
            delayHideTitleBar()
        }
    }




    override fun getLayoutId(): Int {
        return R.layout.activity_new_detail
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

    private fun delayHideTitleBar() {
        hideTitleBarJob?.cancel()
        hideTitleBarJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(viewDataBinding.videoPlayer.dismissControlTime.toLong())
            hideTitleBar()
        }
    }

    private fun delayHideBottomContainer() {
        hideBottomContainerJob?.cancel()
        hideBottomContainerJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(viewDataBinding.videoPlayer.dismissControlTime.toLong())
            viewDataBinding.videoPlayer.getBottomContainer().gone()
            viewDataBinding.videoPlayer.startButton.gone()
        }
    }

    private fun hideTitleBar() {
        viewDataBinding.headerControll.invisibleAlphaAnimation(1000)
        viewDataBinding.ivPullDown.goneAlphaAnimation(1000)
        viewDataBinding.ivCollect.goneAlphaAnimation(1000)
        viewDataBinding.ivMore.goneAlphaAnimation(1000)
        viewDataBinding.ivShare.goneAlphaAnimation(1000)
    }

    private fun showFullScreen() {
        orientaionUtils?.resolveByClick()
    }

    private fun checkArguments() =
        if (intent.getParcelableExtra<NewDetailViewModel.VideoInfo>(EXTRA_VIDEOINFO) == null && intent.getLongExtra(
                EXTRA_VIDEO_ID,
                0L
            ) == 0L
        ) {
            ToastUtils.show(GlobalUtils.getString(R.string.jump_page_unknown_error))
            finish()
            false
        } else {
            true
        }

    private fun initParams() {
        if (intent.getParcelableExtra<NewDetailViewModel.VideoInfo>(EXTRA_VIDEOINFO) != null)
            vm?.videoInfoData = intent.getParcelableExtra(EXTRA_VIDEOINFO)

        if (intent.getLongExtra(EXTRA_VIDEO_ID, 0L) != 0L)
            vm?.videoId = intent.getLongExtra(EXTRA_VIDEO_ID, 0L)

    }

    private fun scrollRepliesToTop() {

        val targetPosition = (relatedAdapter.itemCount - 1) + 2//相关推荐最后一项+1是评论的标题+2是第一条评论
        if (targetPosition < concatAdapter.itemCount - 1) {
            viewDataBinding.recyclerview.smoothScrollToPosition(targetPosition)
        }
    }

    fun scrollTop() {
        if (relatedAdapter.itemCount != 0) {
            viewDataBinding.recyclerview.scrollToPosition(0)
            viewDataBinding.refreshLayout.invisibleAlphaAnimation(2500)
            viewDataBinding.refreshLayout.visibleAlphaAnimation(1500)
        }
    }

    companion object {

        fun start(activity: Activity, videoInfo: NewDetailViewModel.VideoInfo) {
            val starter = Intent(activity, NewDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEOINFO, videoInfo)
            activity.startActivity(starter)
            activity.overridePendingTransition(
                R.anim.ani_push_bottom_in,
                R.anim.ani_push_bottom_out
            )
        }

        fun start(activity: Activity, videoId: Long) {
            val starter = Intent(activity, NewDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEO_ID, videoId)
            activity.startActivity(starter)
            activity.overridePendingTransition(
                R.anim.ani_push_bottom_in,
                R.anim.ani_push_bottom_out
            )
        }
    }

    inner class VideoCallPlayBack : GSYSampleCallBack() {
        //开始加载，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
        override fun onStartPrepared(url: String?, vararg objects: Any?) {
            super.onStartPrepared(url, *objects)
            viewDataBinding.headerControll.gone()
            viewDataBinding.llShare.gone()
        }

        //点击了播放中的空白区域，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
        override fun onClickBlank(url: String?, vararg objects: Any?) {
            super.onClickBlank(url, *objects)
            switchTitleBarVisible()
        }

        //点击了播放状态下的开始按键--->停止，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
        override fun onClickStop(url: String?, vararg objects: Any?) {
            super.onClickStop(url, *objects)
            delayHideBottomContainer()
        }

        //播放完了，objects[0]是title，object[1]是当前所处播放器（全屏或非全屏）
        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            super.onAutoComplete(url, *objects)
            viewDataBinding.headerControll.visible()
            viewDataBinding.ivPullDown.visible()
            viewDataBinding.ivCollect.gone()
            viewDataBinding.ivShare.gone()
            viewDataBinding.ivMore.gone()
            viewDataBinding.llShare.visible()
        }
    }

    inner class ClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            vm?.videoInfoData?.let {
                when (v) {
                    viewDataBinding.ivPullDown -> finish()
                    viewDataBinding.ivMore -> {
                    }
                    viewDataBinding.ivShare -> showDialogShare(
                        this@NewDetailActivity,
                        it.webUrl.raw
                    )
                    viewDataBinding.ivCollect -> ARouter.getInstance()
                        .build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                    viewDataBinding.ivToWechatFriends -> share(it.webUrl.raw, SHARE_WECHAT)
                    viewDataBinding.ivToWechatMemories -> share(
                        it.webUrl.raw,
                        SHARE_WECHAT_MEMORIES
                    )
                    viewDataBinding.ivToWeibo -> share(it.webUrl.raw, SHARE_WEIBO)
                    viewDataBinding.ivToQQ -> share(it.webUrl.raw, SHARE_QQ)
                    viewDataBinding.ivToQQZone -> share(it.webUrl.raw, SHARE_QQZONE)
                    viewDataBinding.ivAvatarComment, viewDataBinding.etComment -> ARouter.getInstance()
                        .build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                    viewDataBinding.ivComment, viewDataBinding.tvComment -> scrollRepliesToTop()
                    else -> {
                    }
                }
            }
        }
    }


}