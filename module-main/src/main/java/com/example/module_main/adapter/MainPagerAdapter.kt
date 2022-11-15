package com.example.module_main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *description : <p>
 *ViewpAger适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 15
 */
class MainPagerAdapter : FragmentStateAdapter {
    private var mFragments: MutableList<Fragment> = mutableListOf()

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    fun setFragment(data: List<Fragment>) {
        mFragments.addAll(data)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return mFragments.size
    }

    override fun createFragment(position: Int): Fragment {
      return  mFragments[position]
    }
}