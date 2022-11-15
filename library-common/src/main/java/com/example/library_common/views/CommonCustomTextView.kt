/*
 * @author flyli
 */

package com.example.library_common.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.example.library_common.R

/**
 *description : <p>
 *使用定制字体的textview
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 15
 */
class CommonCustomTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            val typeArray=context?.obtainStyledAttributes(it, R.styleable.CommonCustomTextView)
            val typefaceType=typeArray?.getInt(R.styleable.CommonCustomTextView_typeface,3)
            initTypeface(typefaceType)
            typeArray?.recycle()
        }

    }

    /**
     * 定制字体
     * @param context Context?
     */
    private fun initTypeface(typefaceType: Int?) {
        //获取资源文件
        val assets = context?.assets
        if (assets != null) {
            //获取自定义的字体
            val font = when(typefaceType){
                1-> Typeface.createFromAsset(assets, "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF")

                2-> Typeface.createFromAsset(assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")

                else -> Typeface.createFromAsset(assets, "fonts/Lobster-1.4.otf")
            }
            setTypeface(font)
        }
    }


}