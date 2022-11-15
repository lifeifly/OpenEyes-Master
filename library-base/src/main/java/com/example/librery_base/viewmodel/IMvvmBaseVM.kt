package com.example.librery_base.viewmodel

import android.view.View
import com.example.librery_base.activity.IBaseView

/**
 *description : <p>
 *定义viewmodel与V的关联
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 22
 */
interface IMvvmBaseVM<V> {
    /**
     * 关联view
     * @param view V
     */
    fun attachUi(view:V)

    /**
     * 获取view
     */
    fun getView():V?

    /**
     * 是否关联view
     */
    fun isAttachUi():Boolean

    /**
     * 解除关联
     */
    fun detachUi()
}