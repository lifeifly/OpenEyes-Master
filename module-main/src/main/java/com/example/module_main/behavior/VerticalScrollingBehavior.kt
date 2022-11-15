package com.example.module_main.behavior

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IntDef
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat


/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 16
 */
abstract class VerticalScrollingBehavior<V : View> : CoordinatorLayout.Behavior<V> {
    //y方向总共未消耗的
    private var mTotalDyUnconsumed = 0

    //y方向总共的位移
    private var mTotalDy = 0

    @ScrollDirection
    private var mOverScrollingDirection = ScrollDirection.SCROLL_NONE

    @ScrollDirection
    private var mScrollDirection = ScrollDirection.SCROLL_NONE

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    /*
     * @return Overscroll direction: SCROLL_DIRECTION_UP, CROLL_DIRECTION_DOWN,
     * SCROLL_NONE
     */
    @ScrollDirection
    fun getOverScrollDirection(): Int {
        return mOverScrollingDirection
    }

    /**
     * @return Scroll direction: SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN,
     *         SCROLL_NONE
     */
    @ScrollDirection
    fun getScrollDirection(): Int {
        return mScrollDirection
    }

    /**
     *
     * @param coordinatorLayout CoordinatorLayout
     * @param child V
     * @param direction Int  Direction of the overscroll: SCROLL_DIRECTION_UP,
     *            SCROLL_DIRECTION_DOWN
     * @param currentOverScroll Int Unconsumed value, negative or positive based on
     *            the direction;
     * @param totalOverScroll Int Cumulative value for current direction
     */
    abstract fun onNestedVerticalOverScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V, @ScrollDirection direction: Int,
        currentOverScroll: Int, totalOverScroll: Int
    )

    /**
     *
     * @param coordinatorLayout CoordinatorLayout
     * @param child V
     * @param target View
     * @param dx Int
     * @param dy Int
     * @param consumed IntArray
     * @param scrollDirection Int  Direction of the overscroll: SCROLL_DIRECTION_UP,
     *            SCROLL_DIRECTION_DOWN
     */
    abstract fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout,
    child: V,target:View,dx:Int,dy:Int,consumed:IntArray,@ScrollDirection scrollDirection:Int)

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int
    ): Boolean {
        return (axes and View.SCROLL_AXIS_VERTICAL)!=0//是否垂直方向
    }

    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        super.onNestedScrollAccepted(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    /**
     * 子view滑动后调用
     * @param coordinatorLayout CoordinatorLayout
     * @param child V
     * @param target View
     * @param dxConsumed Int
     * @param dyConsumed Int
     * @param dxUnconsumed Int
     * @param dyUnconsumed Int
     * @param type Int
     * @param consumed IntArray
     */
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {

        if (dyUnconsumed>0&&mTotalDyUnconsumed<0){
            mTotalDyUnconsumed=0
            mOverScrollingDirection=ScrollDirection.SCROLL_UP
        }else if (dyUnconsumed<0&&mTotalDyUnconsumed>0){
            mTotalDyUnconsumed=0
            mOverScrollingDirection=ScrollDirection.SCROLL_DOWN
        }
        mTotalDyUnconsumed+=dyUnconsumed
        onNestedVerticalOverScroll(coordinatorLayout,child,mOverScrollingDirection,dyConsumed,mTotalDyUnconsumed)
    }

    /**
     * 滑动时调用,由子view传递而来
     * @param coordinatorLayout CoordinatorLayout
     * @param child V
     * @param target View
     * @param dx Int
     * @param dy Int
     * @param consumed IntArray
     * @param type Int
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (dy>0&&mTotalDy<0){
            //向上滑，总偏移小于0
            mTotalDy=0
            mScrollDirection=ScrollDirection.SCROLL_UP
        }else if (dy<0&&mTotalDy>0){
            //向下滑
            mTotalDy=0
            mScrollDirection=ScrollDirection.SCROLL_DOWN
        }
        mTotalDy+=dy
        onDirectionNestedPreScroll(coordinatorLayout,child,target,dx,dy,consumed,mScrollDirection)
    }

    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)

        mScrollDirection=if (velocityY>0)ScrollDirection.SCROLL_UP else ScrollDirection.SCROLL_DOWN
        return onNestedDirectionFling(coordinatorLayout,child,target,velocityX,velocityY,mScrollDirection)
    }

    /**
     * 快速滑动
     * @param coordinatorLayout CoordinatorLayout
     * @param child V
     * @param target View
     * @param velocityX Float
     * @param velocityY Float
     * @param scrollDirection Int
     * @return Boolean
     */
    protected abstract fun onNestedDirectionFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float,
        scrollDirection: Int
    ): Boolean


    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onApplyWindowInsets(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        insets: WindowInsetsCompat
    ): WindowInsetsCompat {
        return super.onApplyWindowInsets(coordinatorLayout, child, insets)
    }

    override fun onSaveInstanceState(parent: CoordinatorLayout, child: V): Parcelable? {
        return super.onSaveInstanceState(parent, child)
    }
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(value = [ScrollDirection.SCROLL_DOWN, ScrollDirection.SCROLL_UP])//限定为SCROLL_UP，SCROLL_DOWN
    annotation class ScrollDirection {
        companion object {
            const val SCROLL_NONE = 0
            const val SCROLL_DOWN = -1
            const val SCROLL_UP = 1
        }
    }
}