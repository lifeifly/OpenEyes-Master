package com.example.librery_base.activity

/**
 *description : <p>
 *界面ui的顶级接口，显示切换流程
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 12
 */
interface IBaseView {
    /**
     * 开始加载
     */
    fun startLoading()

    /**
     * 结束加载
     */
    fun loadFinish()


    /**
     * 加载错误
     */
    fun loadFailed(message:String)
}