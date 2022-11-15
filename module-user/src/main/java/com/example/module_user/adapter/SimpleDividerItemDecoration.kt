package com.example.module_user.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.librery_base.global.dp2px
import com.example.module_user.R

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/2 13
 */
class SimpleDividerItemDecoration(context: Context):RecyclerView.ItemDecoration() {
    //分割线的高度
    private var dividerHeight= dp2px(0.5F)

    /**
     * 分割线左侧的空余距离
     */
    private var leftSpace=0

    /**
     * 控制是否在最后一项item的下面绘制分割线。
     */
    private var showDividerUnderLastItem = false

    private val dividerPaint: Paint = Paint()

    init {
        dividerPaint.color=ContextCompat.getColor(context, R.color.grayDark)
    }
    constructor(context: Context, showDividerUnderLastItem: Boolean) : this(context) {
        this.showDividerUnderLastItem = showDividerUnderLastItem
    }

    constructor(context: Context, leftSpace: Int) : this(context) {
        this.leftSpace = leftSpace
    }
    constructor(context: Context, dividerHeight: Int, @ColorInt dividerColor : Int) : this(context) {
        this.dividerHeight = dividerHeight
        dividerPaint.color = dividerColor
    }
    constructor(context: Context, leftSpace: Int, showDividerUnderLastItem: Boolean, dividerHeight: Int, @ColorInt dividerColor: Int) : this(context) {
        this.showDividerUnderLastItem = showDividerUnderLastItem
        this.leftSpace = leftSpace
        this.dividerHeight = dividerHeight
        dividerPaint.color = dividerColor
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount=parent.childCount
        val left=parent.paddingLeft+leftSpace
        val right=parent.width-parent.paddingRight
        val loopLength=if (showDividerUnderLastItem){
            childCount
        }else{
            childCount-1
        }
        for (i in 0..loopLength-1){
            val view=parent.getChildAt(i)
            val top=view.bottom.toFloat()
            val bottom=(view.bottom+dividerHeight).toFloat()
            c.drawRect(left.toFloat(),top,right.toFloat(),bottom,dividerPaint)
        }
    }


}