package com.example.module_more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_common.fragment.BaseViewPagerFragment
import com.example.library_common.tablayout.TabEntity
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.eventbus.SwitchPagesEvent
import com.example.librery_base.router.RouterFragmentPath
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_more.databinding.FragmentMoreLayoutBinding
import com.example.module_more.interact.InteractFragment
import com.example.module_more.privateletter.PrivateLetterFragment
import com.example.module_more.push.PushFragment
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
@Route(path = RouterFragmentPath.More.PAGER_MORE)
class MoreFragment : BaseViewPagerFragment<FragmentMoreLayoutBinding, IMvvmBaseVM<IBaseView>>() {
    override val createFragment: ArrayList<Fragment>
        get() = arrayListOf(
            PushFragment.newInstance(),
            InteractFragment.newInstance(),
            PrivateLetterFragment.newInstance()
        )
    override val createTitles: ArrayList<CustomTabEntity>
        get() = arrayListOf(TabEntity("推送"), TabEntity("互动"), TabEntity("私信"))

    override fun setViewPager2() {
        viewPager2 = viewDataBinding.viewPager2
    }

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.tab == Navigation.Notification.NOTIFICATION) {
            when (viewPager2?.currentItem) {
                0 -> EventBus.getDefault()
                    .post(RefreshEvent(Navigation.Notification.NOTIFICATION_PUSH))
                1 -> EventBus.getDefault()
                    .post(RefreshEvent(Navigation.Notification.NOTIFICATION_INTERACT))
                2 -> EventBus.getDefault()
                    .post(RefreshEvent(Navigation.Notification.NOTIFICATION_PRIVATE))
                else -> {
                }
            }
        } else if (messageEvent is SwitchPagesEvent) {
            when (messageEvent.tab) {
                Navigation.Notification.NOTIFICATION_PUSH -> viewPager2?.currentItem = 0
                Navigation.Notification.NOTIFICATION_INTERACT -> viewPager2?.currentItem = 1
                Navigation.Notification.NOTIFICATION_PRIVATE -> viewPager2?.currentItem = 2
                else -> {
                }
            }
        }
    }

    override fun getVariable(): Int {
        return 0
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_more_layout
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


}