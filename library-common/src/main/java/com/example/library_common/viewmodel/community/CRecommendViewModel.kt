package com.example.library_common.viewmodel.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_common.Const
import com.example.library_network.GlobalNetwork
import com.example.library_network.api.MainPageService
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import java.lang.Exception

/**
 *description : <p>
 *社区-推荐页面的ViewModel
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 20
 */
class CRecommendViewModel(private val globalNetwork: GlobalNetwork):ViewModel(),IMvvmBaseVM<IBaseView> {
    val dataList=ArrayList<CommunityCommedBean.Item>()

    var nextPageUrl:String?=null

    private val requestParamLiveData=MutableLiveData<String>()

    val dataListLiveData=Transformations.switchMap(requestParamLiveData){
        liveData {
            val result=try {
                val data=globalNetwork.fetchCommunityRecommend(it)
                Result.success(data)
            }catch (e:Exception){
                Result.failure(e)
            }
            emit(result)
        }
    }

    fun onRefresh(){
        requestParamLiveData.value=MainPageService.COMMUNITY_RECOMMEND_URL
    }
    fun onLoadMore(){
        requestParamLiveData.value=nextPageUrl
    }

    override fun attachUi(view: IBaseView) {

    }

    override fun getView(): IBaseView? {
      return null
    }

    override fun isAttachUi(): Boolean {
        return false
    }

    override fun detachUi() {

    }
}