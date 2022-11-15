package com.example.module_community.Recommend

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.library_common.global.InjectViewModel
import com.example.library_common.viewmodel.community.CRecommendViewModel
import com.example.librery_base.eventbus.MsgEvent
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.fragment.MvvmBaseFragment
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.librery_base.utils.ResponseHandler
import com.example.librery_base.utils.ToastUtils
import com.example.module_community.R
import com.example.module_community.databinding.FragmentCrecommendBinding
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 *description : <p>
 *社区-推荐列表
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 20
 */
class CRecommendFragment : MvvmBaseFragment<FragmentCrecommendBinding, CRecommendViewModel>() {
    /**
     * 列表左右两侧间距
     */
    val bothSideSpace = GlobalUtils.getDimension(R.dimen.listSpaceSize)

    /**
     * 列表中间内间距，左and右。
     */
    val middleSpace = dp2px(3f)
    /**
     * 通过获取屏幕宽度来计算出每张图片最大的宽度。
     *
     * @return 计算后得出的每张图片最大的宽度。
     */
    val maxImageWidth: Int
        get() {
            val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay?.getMetrics(metrics)
            val columnWidth = metrics.widthPixels
            return (columnWidth - ((bothSideSpace * 2) + (middleSpace * 2))) / 2
        }

    override val vm: CRecommendViewModel
        get() = ViewModelProvider(
            viewModelStore,
            InjectViewModel.getCommunityRecommendViewModelFactory()
        )
            .get(CRecommendViewModel::class.java)

    private lateinit var mAdaptyer:CRecommendAdapter
    override fun initView(rootView: View) {
        mAdaptyer= CRecommendAdapter(this,vm.dataList)
        val layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy=StaggeredGridLayoutManager.GAP_HANDLING_NONE
        viewDataBinding.recyclerView.layoutManager=layoutManager
        viewDataBinding.recyclerView.adapter=mAdaptyer
        viewDataBinding.recyclerView.addItemDecoration(CRecommendAdapter.ItemDecoration(this))
        viewDataBinding.recyclerView.setHasFixedSize(true)
        viewDataBinding.recyclerView.itemAnimator=null
        viewDataBinding.freshLayout.setOnRefreshListener { vm. onRefresh()}
        viewDataBinding.freshLayout.setOnLoadMoreListener { vm.onLoadMore() }
        observe()
    }

    private fun observe() {
        vm.dataListLiveData.observe(viewLifecycleOwner, Observer {
            val response=it.getOrNull()
            if (response==null){
                Log.d(TAG,"URL2")
                ResponseHandler.getFailureTips(it.exceptionOrNull()).let {
                    if (vm.dataList.isNullOrEmpty()){
                        loadFailed(it)
                    }else{
                        ToastUtils.show(it)
                    }
                }
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            //有数据
            loadFinish()
            vm.nextPageUrl= response.nextPageUrl
            if (response.itemList.isNullOrEmpty()&&vm.dataList.isEmpty()){
                //没有具体数据，上次也没数据
                viewDataBinding.freshLayout.closeHeaderOrFooter()
                return@Observer
            }
            if (response.itemList.isNullOrEmpty()&&vm.dataList.isNotEmpty()){
                //上次有数据，这次没数据
                viewDataBinding.freshLayout.finishLoadMoreWithNoMoreData()
                return@Observer
            }
            when(viewDataBinding.freshLayout.state){
                RefreshState.None,RefreshState.Refreshing->{
                    vm.dataList.clear()
                    vm.dataList.addAll(response.itemList)
                    mAdaptyer.notifyDataSetChanged()
                }
                RefreshState.Loading->{
                    val itemCount=vm.dataList.size
                    vm.dataList.addAll(response.itemList)
                    mAdaptyer.notifyItemRangeInserted(itemCount,response.itemList.size)
                }
                else->{}
            }
            if (response.nextPageUrl.isNullOrEmpty()){
                viewDataBinding.freshLayout.finishLoadMoreWithNoMoreData()
            }else{
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
        showErrorView(message){
            startLoading()
        }
    }

    override fun onMessageEvent(messageEvent: MsgEvent) {
        super.onMessageEvent(messageEvent)
        if (messageEvent is RefreshEvent && messageEvent.tab == Navigation.Community.COMMUNITY_RECOMMEND) {
            viewDataBinding.freshLayout.autoRefresh()
            if (viewDataBinding.recyclerView.adapter?.itemCount ?: 0 > 0) viewDataBinding.recyclerView.scrollToPosition(
                0
            )
        }
    }
    override fun getVariable(): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_crecommend
    }

    override fun onRetryBtnClick() {

    }

    companion object {
        fun newInstance(): CRecommendFragment = CRecommendFragment()
    }

}