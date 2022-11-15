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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.librery_base.R
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.utils.ShareUtils

import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *description : <p>
 *懒加载fragment基类和viewpager配合使用,通过setUserHintVisible()方法的逻辑设计懒加载
 *
 * 可见变不可见取消加载，不可见变可见开始加载
 *</p>
 *
 *@author : flyli
 *@since :2021/5/21 22
 */
abstract class MvvmLazyBaseFragemnt<V : ViewDataBinding, VM : IMvvmBaseVM<IBaseView>> : Fragment(),
    IBaseView {
    //记录当前是否可见
    private var mIsVisivle = false

    protected lateinit var viewDataBinding: V

    protected var vm: VM? = null

    //日志标志
    protected var mFragmentTag = this.javaClass.simpleName

    //根view
    protected var rootView: View? = null

    //布局是否创建
    protected var isViewCreated = false

    //是否第一次可见
    protected var mIsFirstVisible = true

    //加载失败的布局
    private var mLoadErrorView: View? = null

    //显示加载中的控件
    protected var mLoadingProgressBar: ProgressBar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(mFragmentTag, "onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParameter()
        Log.d(mFragmentTag, "onCreate: ")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MsgEvent){

    }

    /**
     * 初始化参数
     */
    private fun initParameter() {
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView == null) {
            viewDataBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                getLayoutId(),
                container,
                false
            )
            rootView = viewDataBinding.root
        }
        isViewCreated = true
        //第一次都被设置为setUserVisibleHint为true，询问是否加载数据，如果首次
        if (!mIsVisivle && userVisibleHint) {
            dispatchEvent(true)
        }
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        Log.d(mFragmentTag, "onCreateView: ")
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(mFragmentTag, "onViewCreated: ")
        //获取viewmodel
        vm = getViewModel()
        if (vm != null) {
            //绑定ui
            vm?.attachUi(this)
        }
        if (getVariable() > 0) {
            viewDataBinding.setVariable(getVariable(), vm)
            viewDataBinding.executePendingBindings()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(mFragmentTag, "onStart")
    }

    /**
     * 预加载的fragment和可见的fragment均会走到onResume
     */
    override fun onResume() {
        super.onResume()
        if (!mIsFirstVisible) {
            //不是第一次可见
            //如果此时进行Activity的跳转，会对所有缓存的fragment进行onResume周期的重复
            //只需对可见的fragment进行加载
            if (!mIsVisivle && userVisibleHint) {
                dispatchEvent(true)
            }
        }
        Log.d(mFragmentTag, "onResume")
    }

    /**
     * 只有当前页面由可见状态变为不可见状态时才需要调用dispatchEvent方法
     *
     */
    override fun onPause() {
        super.onPause()
        if (mIsVisivle && !userVisibleHint) {
            dispatchEvent(false)
        }
        Log.d(mFragmentTag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(mFragmentTag, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        EventBus.getDefault().unregister(this)
        Log.d(mFragmentTag, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        //解绑viewmodel
        if (vm != null && vm?.isAttachUi()!!) {
            vm?.detachUi()
        }
        Log.d(mFragmentTag, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(mFragmentTag, "onDetach")
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
     * 该函数先于其他生命周期的方法执行
     * @param isVisibleToUser Boolean
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated) {//当前视图已被创建
            if (!mIsVisivle && isVisibleToUser) {
                //上次不可见，这次可见,开始加载
                dispatchEvent(true)
            } else if (mIsVisivle && !isVisibleToUser) {
                //上次可见，这次不可见,取消加载
                dispatchEvent(false)
            }
        }

    }

    private fun dispatchEvent(isVisibleToUser: Boolean) {
        //首先考虑fragment嵌套fragment的情况
        if (mIsVisivle == isVisibleToUser) {
            //如果上次状态和这次分发的状态一样就不用处理
            return
        }
        //两个状态不一样，将当前状态赋值
        mIsVisivle = isVisibleToUser
        if (mIsVisivle) {
            //当前可见,父fragment也可见，需要加载数据
            if (mIsFirstVisible) {
                //第一次可见,进行全局初始化
                mIsFirstVisible = false
                onFragmentFirstVisible()
            }
            //加载数据
            onFragmentResume()
            //分发事件给子Fragment
//            dispatchChildEvent(true)
        } else if (!mIsVisivle) {
            //当前不可见，停止加载数据
            //停止加载
            onFragmentPause()
            //分发事件给子Fragment
//            dispatchChildEvent(false)
        }
    }

    /**
     * 停止加载数据
     */
    protected open fun onFragmentPause() {

    }

    /**
     * 分发事件给子Fragment
     * 双重ViewPager嵌套的情况下，第一次滑到Fragment嵌套ViewPager的场景时
     * 此时只会加载外层Fragment的数据，因为在dispatchEvent方法中需要parent可见才会加载，
     * 所以不会加载内嵌viewpager中的fragment数据，因此需要取分发事件给子viewpager的fragment
     */
    private fun dispatchChildEvent(visible: Boolean) {
        val childFragmentManager = childFragmentManager
        val fragments = childFragmentManager.fragments
        //让子Fragment执行dispatchEvetn方法
        fragments.forEach {
            if (it is MvvmLazyBaseFragemnt<*, *> && it.getCurrentVisible()) {
                it.dispatchEvent(visible)
            }
        }
    }

    /**
     * 开始加载数据
     */
    protected open fun onFragmentResume() {

    }

    /**
     * 第一次可见的时候初始化
     */
    protected open fun onFragmentFirstVisible() {

    }

    /**
     * 父fragment是否可见
     * @return Boolean
     */
    private fun parentIsVisible(): Boolean {
        val parentFragment = parentFragment
        if (parentFragment is MvvmLazyBaseFragemnt<*, *>) {
            return parentFragment.getCurrentVisible()
        }
        return true
    }

    /**
     * 获取自身当前是否可见
     * @return Boolean
     */
    private fun getCurrentVisible(): Boolean {
        return mIsVisivle
    }


    /**
     * 子类提供视图绑定的数据id
     * @return Int
     */
    abstract fun getVariable(): Int

    /**
     * 子类提供ViewModel
     * @return VM?
     */
    abstract fun getViewModel(): VM?


    /**
     * 子类提供要加载的布局
     * @return Int
     */
    abstract fun getLayoutId(): Int

    /**
     * 加载失败，重试
     */
    abstract fun onRetryCallback()

    /**
     * 调用系统原生分享
     *
     * @param shareContent 分享内容
     * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
     */
    protected fun share(shareContent: String, shareType: Int) {
        ShareUtils.share(requireActivity(), shareContent, shareType)
    }

    /**
     * 弹出分享对话框
     *
     * @param shareContent 分享内容
     */
    protected fun showDialogShare(shareContent: String) {
        if (this.activity is AppCompatActivity) {
            showDialogShare(requireActivity(), shareContent)
        }
    }
}