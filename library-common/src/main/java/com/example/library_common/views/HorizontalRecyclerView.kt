package com.example.library_common.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 *description : <p>
 *解决嵌套ViewPager时的滑动冲突
 *</p>
 *
 *@author : flyli
 *@since :2021/5/26 12
 */
class HorizontalRecyclerView:RecyclerView {
    private var lastX=0F
    private var lastY=0F
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x=ev.x
        val y=ev.y
        when(ev.action){
            MotionEvent.ACTION_DOWN->{
                //不让parent拦截事件
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE->{
                val deltaX=x-lastX
                val deltaY=y-lastY
                if (abs(deltaX)< abs(deltaY)){
                    //上下滑动
                    //恢复parent可以拦截事件
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        lastX=x
        lastY=y
        return super.dispatchTouchEvent(ev)
    }

}