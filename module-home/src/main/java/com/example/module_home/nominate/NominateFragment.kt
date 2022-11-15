package com.example.module_home.nominate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.home.NominateViewModel
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.utils.ResponseHandler
import com.example.librery_base.utils.ToastUtils
import com.example.module_home.R
import com.example.module_home.databinding.FragmentNominateBinding
import com.example.module_home.nominate.adapter.NominateAdapter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.constant.RefreshState


/**
 *description : <p>
 *首页-推荐列表界面
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 13
 */
class NominateFragment : MvvmBaseFragment<FragmentNominateBinding, NominateViewModel>() {
    override val vm: NominateViewModel
        get() =  ViewModelProvider(this, InjectViewModel.getHomeRecommendViewModelFactory()).get(
            NominateViewModel::class.java
        )
    companion object {
        fun newInstance(): NominateFragment {
            return NominateFragment()
        }
    }

    private lateinit var adapter: NominateAdapter


    override fun initView(rootView: View) {
        adapter = NominateAdapter(this, vm.dataList)
        val layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.recyclerView.layoutManager = layoutManager
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.adapter = adapter
        viewDataBinding.freshLayout.setOnRefreshListener {
            vm.onReresh()
        }
        viewDataBinding.freshLayout.setOnLoadMoreListener {
            vm.loadMore()
        }
        observe()
    }

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && Navigation.Home.HOME_NOMINATE == messageEvent.tab) {
            viewDataBinding.freshLayout.autoRefresh()
            if (viewDataBinding.recyclerView.adapter?.itemCount ?: 0 > 0) viewDataBinding.recyclerView.scrollToPosition(
                0
            )
        }
    }

    private fun observe() {
        vm.dataListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val response = result.getOrNull()
            if (response == null) {
                ResponseHandler.getFailureTips(result.exceptionOrNull())
                    .let { if (vm.dataList.isNullOrEmpty()) loadFailed(it) else ToastUtils.show(it) }
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            loadFinish()
            vm.nextPageUrl = response.nextPageUrl
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
                    adapter.notifyDataSetChanged()
                }
                RefreshState.Loading -> {
                    val itemCount = vm.dataList.size
                    vm.dataList.addAll(response.itemList)

                        adapter.notifyItemRangeInserted(itemCount, response.itemList.size)

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

    override fun getVariable(): Int {
        return 0
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_nominate
    }

    override fun onRetryBtnClick() {

    }

    override fun loadDataOnce() {
        startLoading()
    }

    override fun startLoading() {
        super.startLoading()
        vm.onReresh()
    }

    override fun loadFailed(message: String) {
        super.loadFailed(message)
        showErrorView(message ?: GlobalUtils.getString(R.string.unknown_error)) { startLoading() }
    }


}