package com.example.module_home.discovery

import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_common.Const
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.factory.DiscoveryViewModelFactory
import com.example.library_common.viewmodel.home.DiscoveryViewModel
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.utils.ResponseHandler
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_home.R
import com.example.module_home.databinding.FragmentDiscoveryBinding
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 *description : <p>
 *首页-发现页面
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 13
 */
class DiscoveryFragment : MvvmBaseFragment<FragmentDiscoveryBinding, DiscoveryViewModel>() {
    override val vm: DiscoveryViewModel
        get() = ViewModelProvider(
            viewModelStore,
            InjectViewModel.getHomeDiscoveryViewModelFactory()
        ).get(DiscoveryViewModel::class.java)

    private lateinit var mAdapter: DiscoveryAdapter


    override fun initView(rootView: View) {
        mAdapter = DiscoveryAdapter(this, vm.dataList)
        viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.recyclerView.adapter = mAdapter
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.itemAnimator = null
        viewDataBinding.freshLayout.setOnLoadMoreListener { vm.onLoad() }
        viewDataBinding.freshLayout.setOnRefreshListener { vm.onRefresh() }
        observe()
    }

    private fun observe() {
        vm.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val response = it.getOrNull()
            if (response == null) {
                ResponseHandler.getFailureTips(it.exceptionOrNull()).let {
                    //如果上次也没有数据，则加载失败
                    if (vm.dataList.isEmpty())loadFailed(it) else ToastUtils.show(it)
                }
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            //数据获取到了，停止加载的ui
            loadFinish()
            vm.nextPageUrl = response.nextPageUrl
            if (response.itemList.isNullOrEmpty() && vm.dataList.isEmpty()) {
                //如果数据内部没有数据，并且上次也没有数据，则关闭加载的头部或底部
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            if (response.itemList.isNullOrEmpty() && vm.dataList.isNotEmpty()) {
                //如果上次有数据，这次内部没数据，则显示无数据的底部
                viewDataBinding.freshLayout.finishLoadMoreWithNoMoreData()
                return@Observer
            }
            //说明此次请求有数据，根据状态判断是替换还是插入
            when (viewDataBinding.freshLayout.state) {
                RefreshState.None, RefreshState.Refreshing -> {
                    vm.dataList.clear()
                    vm.dataList.addAll(response.itemList)
                    mAdapter.notifyDataSetChanged()
                }
                RefreshState.Loading -> {
                    vm.dataList.addAll(response.itemList)
                    mAdapter.notifyItemRangeInserted(mAdapter.itemCount, response.itemList.size)
                }
                else -> {

                }
            }
            if (response.nextPageUrl.isNullOrEmpty()) {
                viewDataBinding.freshLayout.finishLoadMoreWithNoMoreData()
            } else {
                viewDataBinding.freshLayout.closeHeaderOrFooter()
            }
        })
    }


    //第一次加载时也是第一次加载时
    override fun loadDataOnce() {
        //开启加载进度
        startLoading()
    }

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.tab == Navigation.Home.HOME_DISCOVERY) {
            viewDataBinding.freshLayout.autoRefresh()
            if (viewDataBinding.recyclerView.adapter?.itemCount ?: 0 > 0) {
                viewDataBinding.recyclerView.scrollToPosition(0)
            }
        }
    }

    override fun startLoading() {
        super.startLoading()
        //刷新viewmodel
        vm.onRefresh()
    }


    @CallSuper
    override fun loadFailed(message: String) {
        super.loadFailed(message)
        //显示加载失败的ui
        showErrorView(message ?: GlobalUtils.getString(R.string.unknown_error)) {
            startLoading()
        }
    }

    override fun getVariable(): Int {
        return 0
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_discovery
    }

    override fun onRetryBtnClick() {

    }


    companion object {
        fun newInstance(): DiscoveryFragment {
            return DiscoveryFragment()
        }
    }
}