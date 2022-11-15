package com.example.library_common.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.library_common.R
import com.example.librery_base.global.dp2px
import com.scwang.smart.refresh.layout.api.RefreshComponent
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent

/**
 *description : <p>
 *SmartRefreshLayout自定义Footer，上拉加载时没有文字和加载进度提示
 *</p>
 *
 *@author : flyli
 *@since :2021/5/28 21
 */
class NoStatusFooter : SimpleComponent, RefreshFooter {
    private var mTextNothing = ""//没有更多数据量
    private var mTitleText: TextView
    private var mFooterHeight = 0
    private var mBackgroundColor = 0
    private var mNoMoreData = false//是否没有更多数据了
    private var mRefreshKernel: RefreshKernel? = null

    constructor(context: Context) : this(
        context,
        null
    )

    constructor(context: Context?, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //加载布局
        View.inflate(context, R.layout.layout_classic_footer, this)
        mTitleText = findViewById(R.id.srl_classics_title)
        val typeArray = context?.obtainStyledAttributes(attrs, R.styleable.NoStatusFooter, 0, 0)
        typeArray?.let {
            if (it.hasValue(R.styleable.NoStatusFooter_srlPrimaryColor)) {
                setPrimaryColor(it.getColor(R.styleable.NoStatusFooter_srlPrimaryColor, 0))
            }
            if (it.hasValue(R.styleable.NoStatusFooter_srlAccentColor)) {
                setAccentColor(it.getColor(R.styleable.NoStatusFooter_srlAccentColor, 0))
            }
            if (it.hasValue(R.styleable.NoStatusFooter_srlTextSizeTitle)) {
                mTitleText.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, it.getDimensionPixelSize(
                        R.styleable.NoStatusFooter_srlTextSizeTitle,
                        dp2px(16F)
                    ).toFloat()
                )
            }
            mTextNothing = when {
                it.hasValue(R.styleable.NoStatusFooter_srlTextNothing) -> {
                    it.getString(R.styleable.NoStatusFooter_srlTextNothing)
                        ?: context.getString(R.string.srl_footer_nothing)
                }
                REFRESH_FOOTER_NOTHING != null -> {
                    REFRESH_FOOTER_NOTHING!!
                }
                else -> {
                    context.getString(R.string.srl_footer_nothing)
                }
            }
        }
        typeArray?.recycle()
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        Log.d(TAG, "onInitialized: height=${height},noMoreData=${mNoMoreData}")
        mRefreshKernel = kernel
        mRefreshKernel?.requestDrawBackgroundFor(this, mBackgroundColor)
        if (mFooterHeight == 0) mFooterHeight = height//获取SmartRefreshLayout全局设置的Footer的高度
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        Log.d(TAG, "onFinish: $success")
        super.onFinish(refreshLayout, success)
        return 0
    }

    /**
     * 设置数据全部加载完成，将不再再次触发加载功能
     * @param noMoreData Boolean
     * @return Boolean
     */
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        Log.d(TAG, "setNoMoreData: $noMoreData")
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            refreshFooterHeight()
            if (noMoreData) {
                mTitleText.text = mTextNothing
            }
        }
        return true
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        super.onStateChanged(refreshLayout, oldState, newState)
        Log.d(TAG, "onStateChanged: newState=${newState},noMoreData=${mNoMoreData}")
        if (!mNoMoreData){
            when(newState){
                RefreshState.None->{}
                RefreshState.PullUpToLoad->{
                    //上拉加载时
                    refreshFooterHeight()
                }
                RefreshState.Loading,RefreshState.LoadReleased->{}
                RefreshState.ReleaseToLoad->{}
                RefreshState.Refreshing->{}
                else->{}
            }
        }else{
            refreshFooterHeight()
        }
    }

    private fun refreshFooterHeight() {
        Log.d(TAG, "refreshFooterHeight: noMoreData=${mNoMoreData}")
        if (mNoMoreData) {
            mRefreshKernel?.refreshLayout?.setFooterHeightPx(mFooterHeight)
        } else {
            //下面有数据，就将Footer的高度设置为0
            mRefreshKernel?.refreshLayout?.setFooterHeight(0F)
        }
        //重新测量高度
        mRefreshKernel?.requestRemeasureHeightFor(this)
    }

    fun setPrimaryColor(@ColorInt primaryColor: Int): NoStatusFooter {
        mBackgroundColor = primaryColor
        mRefreshKernel?.requestDrawBackgroundFor(this, primaryColor)
        return this
    }

    fun setAccentColor(@ColorInt accentColor: Int): NoStatusFooter {
        mTitleText.setTextColor(accentColor)
        return this
    }

    fun setTextTitleSize(size: Float): NoStatusFooter {
        mTitleText.textSize = size
        mRefreshKernel?.requestRemeasureHeightFor(this)
        return this
    }

    fun setAccentColorId(@ColorRes colorId: Int): NoStatusFooter {
        val thisView: View = this
        setAccentColor(ContextCompat.getColor(thisView.context, colorId))
        return this
    }

    companion object {
        const val TAG = "NoStatusFooter"
        var REFRESH_FOOTER_NOTHING: String? = null      //没有更多数据了
    }
}