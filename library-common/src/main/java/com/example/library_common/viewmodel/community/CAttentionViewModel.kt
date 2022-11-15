package com.example.library_common.viewmodel.community

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_network.GlobalNetwork
import com.example.library_network.Repositories
import com.example.library_network.api.MainPageService
import com.example.library_network.bean.CommunityAttentionBean
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import kotlinx.coroutines.*
import java.lang.Exception

/**
 *description : <p>
 *社区-关注页面的ViewModel
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 20
 */
class CAttentionViewModel(private val repositories: Repositories) : ViewModel(),
    IMvvmBaseVM<IBaseView> {
    val dataList = ArrayList<CommunityAttentionBean.Item>()

    var nextPageUrl: String? = null

    private val requestParamLiveData = MutableLiveData<String>()

    var isFirst=true

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                if (isFirst){
                    Log.d("TAG123", ":2 ")
                    val response = repositories.getCABeanFirst()
                    Result.success(response)
                }else{
                    Log.d("TAG123", ":1 "+it)
                    val data = repositories.getCABeanAfter(it)
                    Result.success(data)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }


    fun onRefresh() {
        requestParamLiveData.value = MainPageService.COMMUNITY_ATTENTION_URL
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPageUrl ?: ""
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