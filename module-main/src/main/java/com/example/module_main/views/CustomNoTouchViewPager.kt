package com.example.module_main.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 *description : <p>
 *不可以左右滑动，与底部导航条联动
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 21
 */
class CustomNoTouchViewPager:ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}