package com.example.module_home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.librery_base.router.RouterFragmentPath
import com.example.library_common.tablayout.TabEntity
import com.example.librery_base.activity.IBaseView
import com.example.library_common.fragment.BaseViewPagerFragment
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.eventbus.SwitchPagesEvent
import com.example.librery_base.extension.visible
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_home.daily.DailyFragment
import com.example.module_home.databinding.FragmentHomeLayoutBinding
import com.example.module_home.discovery.DiscoveryFragment
import com.example.module_home.nominate.NominateFragment
import com.flyco.tablayout.listener.CustomTabEntity
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/21 00
 */
@Route(path = RouterFragmentPath.Home.PAGER_HOME)
class HomeFragment : BaseViewPagerFragment<FragmentHomeLayoutBinding, IMvvmBaseVM<IBaseView>>() {

    //获取fragment
    override val createFragment: ArrayList<Fragment> = ArrayList<Fragment>().apply {
        add(DiscoveryFragment.newInstance())
        add(NominateFragment.newInstance())
        add(DailyFragment.newInstance())
    }

    //tab标签的文字
    override val createTitles = ArrayList<CustomTabEntity>().apply {
        add(TabEntity(GlobalUtils.getString(R.string.discovery)))
        add(TabEntity(GlobalUtils.getString(R.string.commend)))
        add(TabEntity(GlobalUtils.getString(R.string.daily)))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivCalendar?.visible()
        viewPager2?.currentItem = 1
    }



    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.tab == Navigation.Home.HOME) {
            when (viewPager2?.currentItem) {
                0 -> EventBus.getDefault().post(RefreshEvent(Navigation.Home.HOME_DISCOVERY))
                1 -> EventBus.getDefault().post(RefreshEvent(Navigation.Home.HOME_NOMINATE))
                2 -> EventBus.getDefault().post(RefreshEvent(Navigation.Home.HOME_DAILY))
                else -> {
                }
            }
        } else if (messageEvent is SwitchPagesEvent) {
            when (messageEvent.tab) {
                Navigation.Home.HOME_DISCOVERY -> viewPager2?.currentItem = 0
                Navigation.Home.HOME_NOMINATE -> viewPager2?.currentItem = 1
                Navigation.Home.HOME_DAILY -> viewPager2?.currentItem = 2
            }
        }
    }


    override fun getVariable(): Int {
        return 0
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_home_layout
    }

    override val vm: IMvvmBaseVM<IBaseView>
        get() = object : IMvvmBaseVM<IBaseView> {
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
        }

    override fun loadDataOnce() {

    }

    override fun initView(rootView: View) {

    }

    override fun onRetryBtnClick() {

    }

    override fun setViewPager2() {
        viewPager2 = viewDataBinding.homeVp
    }
}