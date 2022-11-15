package com.example.module_home.daily

import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.home.DailyViewModel
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
import com.example.module_home.databinding.FragmentDailyBinding
import com.example.module_home.discovery.DiscoveryFragment
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 *description : <p>
 *日报页面
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 13
 */
class DailyFragment : MvvmBaseFragment<FragmentDailyBinding, DailyViewModel>() {
    override val vm: DailyViewModel
        get() = ViewModelProvider(
            viewModelStore,
            InjectViewModel.getHomeDailyViewModelFactory()
        )
            .get(DailyViewModel::class.java)

    private lateinit var mAdapter: DailyAdapter

    override fun initView(rootView: View) {
        mAdapter = DailyAdapter(this, vm.dataList)
        val layoutManager = LinearLayoutManager(activity)
        viewDataBinding.recyclerView.layoutManager = layoutManager
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.adapter = mAdapter
        viewDataBinding.freshLayout.setOnRefreshListener { vm.onRefresh() }
        viewDataBinding.freshLayout.setOnLoadMoreListener { vm.onLoadMore() }
        observe()
    }

    private fun observe() {
        vm.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val response = it.getOrNull()
            if (response == null) {
                ResponseHandler.getFailureTips(it.exceptionOrNull()).let {
                    if (vm.dataList.isEmpty())loadFailed(it) else ToastUtils.show(it)
                }
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            loadFinish()
            vm.nextPage = response.nextPageUrl
            if (response.itemList.isNullOrEmpty() && vm.dataList.isEmpty()) {
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            if (response.itemList.isNullOrEmpty() && vm.dataList.isNotEmpty()) {
                viewDataBinding.freshLayout.finishLoadMoreWithNoMoreData()
                return@Observer
            }
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

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent&&messageEvent.tab==Navigation.Home.HOME_DAILY){
            viewDataBinding.freshLayout.autoRefresh()
            if (viewDataBinding.recyclerView.adapter?.itemCount?:0>0)viewDataBinding.recyclerView.scrollToPosition(0)
        }
    }

    override fun startLoading() {
        super.startLoading()
        vm.onRefresh()
    }

    @CallSuper
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
        return R.layout.fragment_daily
    }

    override fun onRetryBtnClick() {

    }

    override fun loadDataOnce() {
        startLoading()
    }

    companion object {
        fun newInstance(): DailyFragment {
            return DailyFragment()
        }
    }
}