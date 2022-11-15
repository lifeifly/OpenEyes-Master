package com.example.module_main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.adapter.ScreenAutoAdapter
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.router.RouterFragmentPath
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.activity.MvvmBaseActivity
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.eventbus.SwitchPagesEvent
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_main.R
import com.example.module_main.adapter.MainPagerAdapter
import com.example.module_main.databinding.ActivityMainBinding
import com.example.module_main.dialog.DialogAppraiseWorker
import com.gyf.immersionbar.ImmersionBar
import org.greenrobot.eventbus.EventBus

/**
 * app 主页面
 */

@Route(path = RouterActivityPath.Main.PAGER_MAIN)
class MainActivity : MvvmBaseActivity<ActivityMainBinding, IMvvmBaseVM<IBaseView>>() {
    //viewpager的fragment
    private lateinit var fragments: MutableList<Fragment>

    //适配器
    private var adapter: MainPagerAdapter? = null


    private var backPressTime = 0L


    companion object {
        /**
         * 启动该Activity
         * @param context Context
         */
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //适配屏幕
        ScreenAutoAdapter.match(this, 375)
        super.onCreate(savedInstanceState)
        //沉浸式
        ImmersionBar.with(this)
            .statusBarColor(R.color.main_color_bar)//设置状态栏的颜色
            .navigationBarColor(R.color.main_color_bar)//设置导航栏颜色
            .fitsSystemWindows(true)//内容不延长到透明的导航栏和状态栏
            .autoDarkModeEnable(true)
            .init()
        initView()
        initFragment()
    }

    private fun initFragment() {
        fragments = mutableListOf()

        //通过ARouter 获取其他组件提供的fragment
        val homeFragment =
            ARouter.getInstance().build(RouterFragmentPath.Home.PAGER_HOME).navigation()
        val communityFragment =
            ARouter.getInstance().build(RouterFragmentPath.Community.PAGER_COMMUNITY).navigation()
        val moreFragment =
            ARouter.getInstance().build(RouterFragmentPath.More.PAGER_MORE).navigation()
        val userFragment =
            ARouter.getInstance().build(RouterFragmentPath.User.PAGER_USER).navigation()
        if (homeFragment != null) {
            fragments.add(homeFragment as Fragment)
        }
        if (communityFragment != null) {
            fragments.add(communityFragment as Fragment)
        }
        if (moreFragment != null) {
            fragments.add(moreFragment as Fragment)
        }
        if (userFragment != null) {
            fragments.add(userFragment as Fragment)
        }
        adapter = MainPagerAdapter(this)
        adapter?.setFragment(fragments)
//        viewDataBinding.cvContentView.offscreenPageLimit = 1
        viewDataBinding.cvContentView.isUserInputEnabled=false
        viewDataBinding.cvContentView.adapter = adapter
        setTabSelection(0)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            processBackPressed()
        }
    }

    /**
     * EventBus订阅事件
     * @param messageEvent MsgEvent
     */
    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        when {
            messageEvent is SwitchPagesEvent && messageEvent.tab == Navigation.Community.COMMUNITY_RECOMMEND -> {
                //如果事件是切换到社区Fragment-推荐页面,就代码执行点击社区按钮
                viewDataBinding.mainBottomNavigationBar.btnCommunity.performClick()
            }
            else -> {

            }
        }
    }

    override fun startLoading() {

    }

    override fun loadFinish() {

    }

    override fun loadFailed(message: String) {

    }

    fun processBackPressed() {
        val now = System.currentTimeMillis()
        if (now - backPressTime > 2000) {
            ToastUtils.show(
                String.format(
                    GlobalUtils.getString(R.string.press_again_to_exit),
                    GlobalUtils.appName
                )
            )
            backPressTime = now
        } else {
            super.onBackPressed()
        }
    }

    override fun initView() {
        observe()
        setOnClickListener(
            viewDataBinding.mainBottomNavigationBar.btnHome,
            viewDataBinding.mainBottomNavigationBar.btnCommunity,
            viewDataBinding.mainBottomNavigationBar.mainBottomIvRelease,
            viewDataBinding.mainBottomNavigationBar.btnNotification,
            viewDataBinding.mainBottomNavigationBar.btnUser
        ) {
            when (this) {
                viewDataBinding.mainBottomNavigationBar.btnHome -> {
                    notifyBtnRefresh(0)
                    setTabSelection(0)
                }
                viewDataBinding.mainBottomNavigationBar.btnCommunity -> {
                    notifyBtnRefresh(1)
                    setTabSelection(1)
                }
                viewDataBinding.mainBottomNavigationBar.mainBottomIvRelease -> {
                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN)
                        .navigation()
                }
                viewDataBinding.mainBottomNavigationBar.btnNotification -> {
                    notifyBtnRefresh(2)
                    setTabSelection(2)

                }
                viewDataBinding.mainBottomNavigationBar.btnUser -> {
                    notifyBtnRefresh(3)
                    setTabSelection(3)
                }
            }
        }
    }

    /**
     * 将被选中的按钮设置为true，并跳转到对应的fragment
     * @param index Int
     */
    private fun setTabSelection(index: Int) {
        clearAllSelected()
        when (index) {
            0 -> {
                viewDataBinding.mainBottomNavigationBar.mainBottomIvHome.isSelected = true
                viewDataBinding.mainBottomNavigationBar.mainBottomTvHome.isSelected = true

                viewDataBinding.cvContentView.setCurrentItem(0)
            }
            1 -> {
                viewDataBinding.mainBottomNavigationBar.mainBottomIvCommunity.isSelected = true
                viewDataBinding.mainBottomNavigationBar.mainBottomTvCommunity.isSelected = true

                viewDataBinding.cvContentView.setCurrentItem(1)
            }
            2 -> {
                viewDataBinding.mainBottomNavigationBar.mainBottomIvNotification.isSelected = true
                viewDataBinding.mainBottomNavigationBar.mainBottomTvNotification.isSelected = true

                viewDataBinding.cvContentView.setCurrentItem(2)
            }
            3 -> {
                viewDataBinding.mainBottomNavigationBar.mainBottomIvUser.isSelected = true
                viewDataBinding.mainBottomNavigationBar.mainBottomTvUser.isSelected = true

                viewDataBinding.cvContentView.setCurrentItem(3)
            }
        }
    }

    /**
     * 将所有的tab设置为isSelected=false
     */
    private fun clearAllSelected() {
        viewDataBinding.mainBottomNavigationBar.mainBottomIvHome.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomTvHome.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomIvCommunity.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomTvCommunity.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomIvRelease.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomIvNotification.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomTvNotification.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomIvUser.isSelected = false
        viewDataBinding.mainBottomNavigationBar.mainBottomTvUser.isSelected = false
    }

    /**
     * 选中后更新Ui
     * @param index Int
     */
    private fun notifyBtnRefresh(index: Int) {
        when (index) {
            0 -> {
                if (viewDataBinding.mainBottomNavigationBar.mainBottomIvHome.isSelected)
                    EventBus.getDefault()
                        .post(RefreshEvent(Navigation.Home.HOME))

            }
            1 -> {
                if (viewDataBinding.mainBottomNavigationBar.mainBottomIvCommunity.isSelected)
                    EventBus.getDefault()
                        .post(RefreshEvent(Navigation.Community.COMMUNITY))
            }
            2 -> {
                if (viewDataBinding.mainBottomNavigationBar.mainBottomIvNotification.isSelected)
                    EventBus.getDefault()
                        .post(RefreshEvent(Navigation.Notification.NOTIFICATION))
            }
            3 -> {
                if (viewDataBinding.mainBottomNavigationBar.mainBottomIvUser.isSelected)
                    EventBus.getDefault()
                        .post(RefreshEvent(Navigation.Mine.MINE))
            }
        }
    }

    private fun observe() {
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(DialogAppraiseWorker.showDialogRequest.id)
            .observe(this, Observer {
                Log.d(TAG, "observe: workInfo.state = ${it.state}")
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    WorkManager.getInstance(this).cancelAllWork()
                } else if (it.state == WorkInfo.State.RUNNING) {
                    if (isActive) {
                        DialogAppraiseWorker.showDialog(this)
                        WorkManager.getInstance(this).cancelAllWork()
                    }
                }
            })
    }

    override fun getViewModel(): IMvvmBaseVM<IBaseView>? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getBindingVariable(): Int {
        return 0
    }

    override fun onRetryClick() {

    }


}