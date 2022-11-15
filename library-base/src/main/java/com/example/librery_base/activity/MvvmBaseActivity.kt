package com.example.librery_base.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.utils.ShareUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.umeng.message.PushAgent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *description : <p>
 *activity抽象基类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 22
 */
abstract class MvvmBaseActivity<V:ViewDataBinding,VM:IMvvmBaseVM<IBaseView>>:AppCompatActivity(),IBaseView {
    //需要绑定的viewmodel
    protected var vm:VM?=null

    //需要和viewmodel绑定的databinding
    protected lateinit var viewDataBinding:V


    /**
     * 判断当前Activity是否在前台。
     */
    protected var isActive: Boolean = false
    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${this.javaClass.simpleName}:onCreate ")
        //初始化viewmodel
        initViewModel()
        //初始化databinding，绑定ui和viewmodel
        performDatading()
        initView()
        PushAgent.getInstance(this).onAppStart()
        //订阅事件
        EventBus.getDefault().register(this)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "${this.javaClass.simpleName}:onStart ")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "${this.javaClass.simpleName}:onResume ")
        isActive=true
    }
    @Subscribe(threadMode = ThreadMode.MAIN)//主线程执行订阅方法
    open fun onMessageEvent(messageEvent:MsgEvent){

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "${this.javaClass.simpleName}:onPause ")
        isActive=false
    }
    /**
     * 解除绑定
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "${this.javaClass.simpleName}:onDestroy ")
        if (vm!=null&&vm?.isAttachUi()!!){
            vm?.detachUi()
        }
        EventBus.getDefault().unregister(this)
    }

    /**
     * 创建databinding
     */
    private fun performDatading() {
        viewDataBinding=DataBindingUtil.setContentView(this,getLayoutId())
        //如果vm没有初始化，重新赋值
        this.vm=if (vm==null)getViewModel() else vm
        if (getBindingVariable()>0){
            //为布局设置绑定的viewmodel,将视图和viewmodel绑定
            viewDataBinding.setVariable(getBindingVariable(),vm)
        }
        viewDataBinding.executePendingBindings()

    }

    /**
     * 初始化viewmodel
     */
    private fun initViewModel() {
        vm=getViewModel()
        if (vm!=null){
            //绑定ui
            vm?.attachUi(this)
        }
    }


    abstract fun initView()

    /**
     * 子类创建自己的viewmodel
     * @return VM
     */
    protected abstract fun getViewModel():VM?

    /**
     * 子类的布局
     * @return Int
     */
    protected abstract fun getLayoutId():Int

    /**
     * 获取参数variable
     * @return Int
     */
    protected abstract fun getBindingVariable():Int

    /**
     * 失败重试
     */
    protected abstract fun onRetryClick()

    /**
     * 调用系统原生分享
     *
     * @param shareContent 分享内容
     * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
     */
    protected fun share(shareContent: String, shareType: Int) {
        ShareUtils.share(this, shareContent, shareType)
    }
}