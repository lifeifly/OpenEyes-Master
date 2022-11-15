package com.example.librery_base.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.librery_base.R
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.viewmodel.IMvvmBaseVM
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *description : <p>
 *fragment基类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/21 21
 */
abstract class MvvmBaseFragment<V : ViewDataBinding, VM : IMvvmBaseVM<IBaseView>> : Fragment(),
    IBaseView {

    protected abstract val vm: VM

    private var mLoadingProgressBar: ProgressBar? = null

    protected var mLoadErrorView: View? = null

    protected lateinit var viewDataBinding: V


    //fragment的tag
    protected val mFragmentTag = this.javaClass.simpleName

    private var rootView: View? = null

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    /**
     * 是否已经加载过数据
     */
    private var mHasLoadedData = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(mFragmentTag, " : onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParamters()
        Log.d(mFragmentTag, " : onCreate")
    }

    /**
     * 初始化参数
     */
    private fun initParamters() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(mFragmentTag, " : onCreateView")
        if (rootView == null) {
            viewDataBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    getLayoutId(),
                    container,
                    false
                )
            rootView = viewDataBinding.root
        }

        mLoadingProgressBar = rootView!!.findViewById(R.id.loading)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        return viewDataBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(mFragmentTag, " : onViewCreated")

        vm.attachUi(this)

        if (getVariable() > 0) {
            //和视图绑定的变量存在，则设置viewmodel和视图绑定
            viewDataBinding.setVariable(getVariable(), vm)
            viewDataBinding.executePendingBindings()
        }
        initView(rootView!!)
    }

    override fun onResume() {
        super.onResume()
        Log.d(mFragmentTag, " : onResume")

        //当Fragment在屏幕上可见并且没有加载过数据时和父fragment加载数据后调用，
        if (!mHasLoadedData && getParentHasLoadData()) {
            loadDataOnce()
            //一般都是子fragment先一步执行onResume,所以说父fragment在子fragment之后执行onResume方法，而子Fragment并没有加载数据，需要补发一次
            dispatchChildToLoadOnce()
            Log.d(TAG, "BaseFragment-->loadDataOnce()")
            mHasLoadedData = true
        }
    }

    /**
     * 分发给子Fragment，通知子Fragment他们的父fragment已经可见并加载了数据
     */
    private fun dispatchChildToLoadOnce() {
        //如果子Fragment执行到了onResume方法，并且没有加载数据，那么子Fragment去加载数据
        childFragmentManager.fragments.forEach {
            if (it is MvvmBaseFragment<*, *> && !it.getSelfHasLoadData() && it.isResumed) {
                it.loadDataOnce()
            }
        }
    }

    /**
     * 过去父framgent的加载数据的信息,如果没有父fragment，默认上一层是可见的
     * @return Boolean
     */
    fun getParentHasLoadData(): Boolean {
        Log.d(TAG, "getParentHasLoadData: 1"+parentFragment?.javaClass?.simpleName )
        if (parentFragment != null && parentFragment is MvvmBaseFragment<*, *>) {
            Log.d(TAG, "getParentHasLoadData: "+(parentFragment as MvvmBaseFragment<*, *>).getSelfHasLoadData())
            return (parentFragment as MvvmBaseFragment<*, *>).getSelfHasLoadData()
        }
        return true
    }

    fun getSelfHasLoadData(): Boolean {
        return mHasLoadedData
    }

    abstract fun loadDataOnce()

    override fun onPause() {
        super.onPause()
        Log.d(mFragmentTag, " : onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(mFragmentTag, " : onDestroyView")
        mHasLoadedData=false
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (vm.isAttachUi()) {
            //解绑
            vm.detachUi()
        }
        Log.d(mFragmentTag, " : onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        if (vm.isAttachUi()) {
            vm.detachUi()
        }
        Log.d(mFragmentTag, " : onDetach")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MsgEvent) {

    }

    /**
     * 开始加载
     */
    @CallSuper
    override fun startLoading() {
        mLoadingProgressBar?.visibility = View.VISIBLE
        hideLoadErrorView()
    }


    /**
     * 加载结束
     */
    @CallSuper
    override fun loadFinish() {
        mLoadingProgressBar?.visibility = View.GONE
    }

    /**
     * 加载失败
     * @param message String
     */
    @CallSuper
    override fun loadFailed(message: String) {
        mLoadingProgressBar?.visibility = View.GONE
    }

    /**
     * 显示加载失败视图
     * @param tip String
     * @param block 重试函数
     */
    fun showErrorView(tip: String, block: View.() -> Unit) {
        if (mLoadErrorView != null) {
            mLoadErrorView?.visibility = View.VISIBLE
            return
        }
        if (rootView != null) {
            val viewStub = rootView?.findViewById<ViewStub>(R.id.loadErrorView)
            if (viewStub != null) {
                mLoadErrorView = viewStub.inflate()
                val loadErrorText = mLoadErrorView?.findViewById<TextView>(R.id.loadErrorText)
                loadErrorText?.text = tip
                val loadErrorContainer = mLoadErrorView?.findViewById<View>(R.id.loadErrorContainer)
                loadErrorContainer?.setOnClickListener {
                    it?.block()
                }
            }
        }
    }

    /**
     * 隐藏加载失败的view
     */
    private fun hideLoadErrorView() {
        mLoadErrorView?.visibility = View.GONE
    }

    /**
     * 初始化view
     * @param rootView View
     */
    abstract fun initView(rootView: View)

    /**
     * 子类提供视图绑定的变量id
     * @return Int
     */
    protected abstract fun getVariable(): Int


    /**
     * 子类提供布局
     * @return Int
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 加载失败重试的方法
     */
    protected abstract fun onRetryBtnClick()
}