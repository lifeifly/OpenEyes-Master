package com.example.module_main.behavior

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.google.android.material.snackbar.Snackbar
import java.lang.IllegalArgumentException


/**
 *description : <p>
 *导航视图的behavior
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 16
 */
class BottomNavigationViewBehavior<V:View>:VerticalScrollingBehavior<V> {


    companion object{
        //插值器
        private val INTERPOLATOR=LinearOutSlowInInterpolator()

        fun <V:View> from(view:V):BottomNavigationViewBehavior<V>{
            val params=view.layoutParams
            if (!(params is CoordinatorLayout.LayoutParams)){
                throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            }
            //获取behavior
            val behavior=params.behavior
            if (!(behavior is BottomNavigationViewBehavior)){
                throw IllegalArgumentException("The view is not associated with BottomNavigationBehavior")
            }
            return behavior as BottomNavigationViewBehavior<V>
        }
    }
    private val mWithSnackBarImpl= if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)LolopopBottomNavWithSnackBarImpl()
    else PreLolopopBottomNavWithSnackBarImpl()

    private var isTablet=false

    private var mTabLayoutId=0

    private var hidden=false

    private var mOffsetValueAnimator:ViewPropertyAnimator?=null

    private var mTabLayout:ViewGroup?=null

    private var mTabsHolder:View?=null

    private var mSnackBarHeight=-1

    private var scrollingEnabled=true

    private var hideAlongSnackBar=false

    private var attrsAtray= intArrayOf(android.R.attr.id)

    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        val a=context?.obtainStyledAttributes(attrs,attrsAtray)
        mTabLayoutId= a?.getResourceId(0,View.NO_ID)!!
        a.recycle()
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        mWithSnackBarImpl.updateSnackBar(parent,dependency,child)
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout, child: V, dependency: View) {
        updateScrollingForSnackbar(dependency,child,true)
        super.onDependentViewRemoved(parent, child, dependency)
    }

    private fun updateScrollingForSnackbar(dependency: View, child: V, enabled: Boolean) {
        if (!isTablet&&dependency is Snackbar.SnackbarLayout){
            scrollingEnabled=enabled
            if (!hideAlongSnackBar&&child.translationY!=0F){
                child.translationY=0F
                hidden=false
                hideAlongSnackBar=true
            }else if (hideAlongSnackBar){
                hidden=true
                animateOffset(child,-child.height)
            }
        }
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {
        updateScrollingForSnackbar(dependency,child,false)
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        val layoutChild=super.onLayoutChild(parent, child, layoutDirection)
        if (mTabLayout==null&&mTabLayoutId!=View.NO_ID){
            mTabLayout=findTabLayout(child)
            getTabsHolder()
        }
        return layoutChild
    }

    private fun getTabsHolder() {
        if (mTabsHolder!=null){
            mTabsHolder=mTabLayout?.getChildAt(0)
        }
    }

    private fun findTabLayout(child: V): ViewGroup? {
        if (mTabLayoutId==0){
            return null
        }
        return child.findViewById(mTabLayoutId)
    }



    override fun onNestedVerticalOverScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        direction: Int,
        currentOverScroll: Int,
        totalOverScroll: Int
    ) {
    }

    override fun onDirectionNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        scrollDirection: Int
    ) {
        handleDirection(child,scrollDirection)
    }

    private fun handleDirection(child: V, scrollDirection: Int) {
        if (!scrollingEnabled){
            return
        }
        if (scrollDirection==ScrollDirection.SCROLL_DOWN&&hidden){
            hidden=false
            animateOffset(child,0)
        }else if (scrollDirection==ScrollDirection.SCROLL_UP&&!hidden){
            hidden=true
            animateOffset(child,child.height)
        }
    }

    override fun onNestedDirectionFling(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        velocityX: Float,
        velocityY: Float,
        scrollDirection: Int
    ): Boolean {
        handleDirection(child, scrollDirection)
        return true
    }


    private fun animateOffset(child: V, height: Int) {
        ensureOrCancelAnimator(child)
        mOffsetValueAnimator?.translationY(height.toFloat())?.start()
        animateTabsHolder(height)
    }

    private fun ensureOrCancelAnimator(child: V) {
        if (mOffsetValueAnimator==null){
            mOffsetValueAnimator=child.animate().setDuration(100).setInterpolator(INTERPOLATOR)
        }else{
            mOffsetValueAnimator?.cancel()
        }
    }

    private fun animateTabsHolder(offset: Int) {
        if (mTabsHolder!=null){
            val off=if (offset>0)0F else 1F
            ViewCompat.animate(mTabsHolder!!)
                .alpha(off)
                .setDuration(200)
                .start()
        }
    }

    fun isScrollingEnabled():Boolean=scrollingEnabled

    fun setScrollingEnabled(enabled:Boolean){
        this.scrollingEnabled=enabled
    }

    fun setHidden(view:V,bottomLayoutHidden:Boolean){
        if (!bottomLayoutHidden&&hidden){
            animateOffset(view,0)
        }else if (bottomLayoutHidden&&!hidden){
            animateOffset(view,-view.height)
        }
        hidden=bottomLayoutHidden
    }

    private interface BottomNavigationWithSnackBarImpl{
        fun updateSnackBar(coordinatorLayout: CoordinatorLayout,dependency:View,child:View)
    }
    //6.0之前
    private inner class PreLolopopBottomNavWithSnackBarImpl:BottomNavigationWithSnackBarImpl{
        override fun updateSnackBar(
            coordinatorLayout: CoordinatorLayout,
            dependency: View,
            child: View
        ) {
            if (!isTablet&&dependency is Snackbar.SnackbarLayout){
                if (mSnackBarHeight==-1){
                    mSnackBarHeight=dependency.height
                }
                val targetPadding=child.measuredHeight

                val shadow=ViewCompat.getElevation(child)
                val params=dependency.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin= (targetPadding-shadow).toInt()
                child.bringToFront()
                if (Build.VERSION.SDK_INT<Build.VERSION_CODES.KITKAT){
                    child.parent.requestLayout()
                    (child.parent as View).invalidate()
                }
            }
        }

    }
    //6.0之后
    private inner class LolopopBottomNavWithSnackBarImpl:BottomNavigationWithSnackBarImpl{
        override fun updateSnackBar(
            coordinatorLayout: CoordinatorLayout,
            dependency: View,
            child: View
        ) {
            if (!isTablet&&dependency is Snackbar.SnackbarLayout){
                if (mSnackBarHeight==-1){
                    mSnackBarHeight=dependency.height
                }
                val targetPadding=mSnackBarHeight+child.measuredHeight
                dependency.setPadding(dependency.paddingLeft,
                dependency.paddingTop,
                dependency.paddingRight,
                targetPadding)
            }
        }

    }
}