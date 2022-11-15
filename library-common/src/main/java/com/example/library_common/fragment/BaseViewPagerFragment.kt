package com.example.library_common.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.library_common.search.SearchFragment
import com.example.librery_base.R
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.fragment.MvvmLazyBaseFragemnt
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import java.util.*

/**
 *description : <p>
 *使用viewpager2+tablayout的fragment
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 09
 */

abstract class BaseViewPagerFragment<V : ViewDataBinding, VM : IMvvmBaseVM<IBaseView>> :
    MvvmBaseFragment<V, VM>() {
    //左侧图标
    protected var ivCalendar: ImageView? = null

    //右侧图标
    protected var ivSearch: ImageView? = null

    protected var viewPager2: ViewPager2? = null

    protected var tabLayout: CommonTabLayout? = null

    protected var pageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    protected var adapter: FragmentStateAdapter? = null

    protected var offScreenLimit = 1

    abstract val createFragment: ArrayList<Fragment>

    abstract val createTitles: ArrayList<CustomTabEntity>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=super.onCreateView(inflater, container, savedInstanceState)
        initView()
        setViewPager2()
        initViewPager()
        return view
    }

    abstract fun setViewPager2()


    private fun initView() {
        ivCalendar = viewDataBinding.root.findViewById(R.id.title_calendar)
        ivSearch = viewDataBinding.root.findViewById(R.id.title_iv_search)
        tabLayout = viewDataBinding.root.findViewById(R.id.title_tablayout)

        setOnClickListener(ivCalendar, ivSearch) {
            if (this == ivCalendar) {
                ToastUtils.show(R.string.currently_not_supported)
            } else if (this == ivSearch) {
                SearchFragment.switchFragment(requireActivity())
            }
        }
    }

    private fun initViewPager() {
        viewPager2?.offscreenPageLimit = offScreenLimit
        adapter=Viewpager2Adapter(this)
        viewPager2?.adapter = adapter

        tabLayout?.setTabData(createTitles)
        tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager2?.setCurrentItem(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
        pageChangeCallback = PageChangeCallback()
        viewPager2?.registerOnPageChangeCallback(pageChangeCallback!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        pageChangeCallback?.run {
            viewPager2?.unregisterOnPageChangeCallback(this)
            pageChangeCallback = null
        }
    }



    inner class PageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            Log.d("TAG", "onPageSelected: "+position)
            tabLayout?.currentTab = position
        }
    }
    //因为此ViewPager存在于fragment中，可以用此构造方法用于ViewPager2中子Fragment获取ViewPager2的父fragment
    inner class Viewpager2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


        override fun getItemCount(): Int = createFragment.size

        override fun createFragment(position: Int): Fragment = createFragment[position]


    }
}