package com.example.module_community

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
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.fragment.MvvmLazyBaseFragemnt
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.router.RouterFragmentPath
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_community.Recommend.CRecommendFragment
import com.example.module_community.attention.CAttentionFragment
import com.example.module_community.databinding.FragmentCommunityLayoutBinding
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
@Route(path = RouterFragmentPath.Community.PAGER_COMMUNITY)
class CommunityFragment :
    BaseViewPagerFragment<FragmentCommunityLayoutBinding, IMvvmBaseVM<IBaseView>>() {

    override val createFragment: ArrayList<Fragment>
        get() = arrayListOf(CRecommendFragment.newInstance(), CAttentionFragment.newInstance())
    override val createTitles: ArrayList<CustomTabEntity>
        get() = arrayListOf(
            TabEntity(GlobalUtils.getString(R.string.commend)),
            TabEntity(GlobalUtils.getString(R.string.follow))
        )

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.tab == Navigation.Community.COMMUNITY) {
            when (viewPager2?.currentItem) {
                0 -> {
                    EventBus.getDefault()
                        .post(RefreshEvent(Navigation.Community.COMMUNITY_RECOMMEND))
                }
                1 -> {
                    EventBus.getDefault()
                        .post(RefreshEvent(Navigation.Community.COMMUNITY_ATTENTION))
                }
                else -> {
                }
            }
        }else if (messageEvent is SwitchPagesEvent){
            when(messageEvent.tab){
                Navigation.Community.COMMUNITY_RECOMMEND->viewPager2?.currentItem=0
            }
        }
    }

    override fun setViewPager2() {
        viewPager2 = viewDataBinding.viewPager
        viewPager2?.currentItem=0
    }

    override fun getVariable(): Int {
        return 0
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_community_layout
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