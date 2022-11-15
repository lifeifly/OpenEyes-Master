package com.example.module_community.attention

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.community.CAttentionViewModel
import com.example.library_common.viewmodel.factory.CAttentionViewModelFactory
import com.example.library_network.GlobalNetwork
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.utils.ResponseHandler
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.module_community.R
import com.example.module_community.databinding.FragmentCattentionBinding
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.shuyu.gsyvideoplayer.GSYVideoManager

/**
 *description : <p>
 *社区-关注页面
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 20
 */
class CAttentionFragment : MvvmBaseFragment<FragmentCattentionBinding, CAttentionViewModel>() {
    override val vm: CAttentionViewModel
        get() = ViewModelProvider(
            viewModelStore,
            InjectViewModel.getCommunityAttentionViewModelFactory()
        )
            .get(CAttentionViewModel::class.java)
    private lateinit var mAdapter: CAttentionAdapter


    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.tab == Navigation.Community.COMMUNITY_ATTENTION) {
            viewDataBinding.freshLayout.autoRefresh()
            if (viewDataBinding.recyclerView.adapter?.itemCount ?: 0 > 0) viewDataBinding.recyclerView.scrollToPosition(
                0
            )
        }
    }

    override fun initView(rootView: View) {
        mAdapter = CAttentionAdapter(this, vm.dataList)
        viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.recyclerView.adapter = mAdapter
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.itemAnimator = null
        //设置滚动监听器，控制自动播放和暂停
        viewDataBinding.recyclerView.addOnScrollListener(
            AutoPlayScrollListener(
                R.id.videoPlayer,
                AutoPlayScrollListener.PLAY_RANGE_TOP,
                AutoPlayScrollListener.PLAY_RANGE_BOTTOM
            )
        )
        viewDataBinding.freshLayout.setOnRefreshListener { vm.onRefresh() }
        viewDataBinding.freshLayout.setOnLoadMoreListener { vm.onLoadMore() }
        observe()
    }

    private fun observe() {
        vm.dataListLiveData.observe(viewLifecycleOwner, Observer { result ->
            val response = result.getOrNull()
            if (response == null) {
                if (vm.isFirst){
                    //第一次加载数据库中的数据为null，直接将first置为false，并再次刷新
                    Log.d("TAG123", ":0 ")
                    vm.isFirst=false
                    vm.onRefresh()
                }else{
                    ResponseHandler.getFailureTips(result.exceptionOrNull()).let {
                        if (vm.dataList.isNullOrEmpty()) loadFailed(it) else ToastUtils.show(it)
                    }
                }

                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            //从数据库加载到了数据，置为false
            vm.isFirst=false
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
                    mAdapter.notifyDataSetChanged()
                }
                RefreshState.Loading -> {
                    val itemCount = vm.dataList.size
                    vm.dataList.addAll(response.itemList)
                    mAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)
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

    override fun loadDataOnce() {
        startLoading()
    }

    override fun startLoading() {
        super.startLoading()
        vm.onRefresh()
    }

    override fun loadFailed(message: String) {
        super.loadFailed(message)
        showErrorView(message) {
            startLoading()
        }
    }

    override fun getVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_cattention
    }

    override fun onRetryBtnClick() {

    }

    companion object {
        fun newInstance(): CAttentionFragment = CAttentionFragment()
    }


}