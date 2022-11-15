package com.example.module_more.push

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_common.fragment.BaseViewPagerFragment
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.notification.PushViewModel
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.utils.ResponseHandler
import com.example.librery_base.utils.ToastUtils
import com.example.module_more.R
import com.example.module_more.databinding.FragmentPushBinding
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 *description : <p>
 *通知-推送
 *</p>
 *
 *@author : flyli
 *@since :2021/6/2 16
 */
class PushFragment:MvvmBaseFragment<FragmentPushBinding,PushViewModel>() {
    override val vm: PushViewModel
        get() = ViewModelProvider(viewModelStore,InjectViewModel.getPushViewModelFactory()).get(PushViewModel::class.java)

    private lateinit var mAdapter:PushAdapter

    override fun loadDataOnce() {
        startLoading()
    }

    override fun initView(rootView: View) {
        mAdapter= PushAdapter(this,vm.dataList)
        viewDataBinding.recyclerView.layoutManager=LinearLayoutManager(requireActivity())
        viewDataBinding.recyclerView.adapter=mAdapter
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.itemAnimator=null
        viewDataBinding.freshLayout.setOnRefreshListener { vm.onRefresh() }
        viewDataBinding.freshLayout.setOnLoadMoreListener { vm.onLoadMore() }
        observe()
    }

    private fun observe() {
        vm.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val response = it.getOrNull()
            if (response == null) {
                ResponseHandler.getFailureTips(it.exceptionOrNull()).let {
                    if (vm.dataList.isEmpty()) loadFailed(it) else ToastUtils.show(it)
                    viewDataBinding.freshLayout.closeHeaderOrFooter()
                    return@Observer
                }
            }
            loadFinish()
            vm.nextPage = response.nextPageUrl
            if (response.messageList.isNullOrEmpty() && vm.dataList.isEmpty()) {
                //首次进入页面时，获取数据条目为0时处理。
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            if (response.messageList.isNullOrEmpty() && !vm.dataList.isNotEmpty()) {
                //上拉加载数据时，返回数据条目为0时处理。
                viewDataBinding.freshLayout.finishLoadMoreWithNoMoreData()
                return@Observer
            }
            when (viewDataBinding.freshLayout.state) {
                RefreshState.None, RefreshState.Refreshing -> {
                    vm.dataList.clear()
                    vm.dataList.addAll(response.messageList)
                    mAdapter.notifyDataSetChanged()
                }
                RefreshState.Loading -> {
                    val itemCount = vm.dataList.size
                    vm.dataList.addAll(response.messageList)
                    mAdapter.notifyItemRangeInserted(itemCount, response.messageList.size)
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

    override fun startLoading() {
        super.startLoading()
        vm.onRefresh()
    }

    override fun loadFailed(message: String) {
        super.loadFailed(message)
        showErrorView(message){
            startLoading()
        }
    }

    override fun getVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
      return R.layout.fragment_push
    }

    override fun onRetryBtnClick() {

    }
    companion object{
        fun newInstance():PushFragment{
            return PushFragment()
        }
    }
}