package com.example.librery_base.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.librery_base.model.SuperBaseModel
import java.lang.ref.Reference
import java.lang.ref.WeakReference

/**
 *description : <p>
 *管理V M
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 23
 */
abstract class MvvmBaseViewModel<V,M:SuperBaseModel<*>>:ViewModel(),IMvvmBaseVM<V> {
    //需要绑定的view
    private var mUiRef:Reference<V>?=null
    //需要绑定的数据model
    protected var model:M?=null

    /**
     * 绑定ui
     * @param view V
     */
    override fun attachUi(view: V) {
        mUiRef=WeakReference(view)
    }

    /**
     * 获取ui
     * @return View
     */
    override fun getView(): V? {
       if (mUiRef==null){
           return null
       }
        if (mUiRef?.get()!=null){
            return mUiRef?.get()
        }
        return null
    }

    /**
     * 是否绑定ui
     * @return Boolean
     */
    override fun isAttachUi(): Boolean {
        return mUiRef== null&&mUiRef?.get()==null
    }

    /**
     * 解除绑定
     */
    override fun detachUi() {
        if (mUiRef==null)return
        if (mUiRef?.get()!=null){
            mUiRef?.clear()
            mUiRef=null
        }
        if (model!=null){
            model?.cancel()
        }
    }

    protected fun loadData(){}
    //初始化model，交给子类完成
    protected abstract fun initModel()
}