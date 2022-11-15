package com.example.library_common.viewmodel.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_network.GlobalNetwork
import com.example.library_network.api.MainPageService
import com.example.library_network.bean.HomeCommendBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import java.lang.Exception

/**
 *description : <p>
 *首页-推荐的ViewModel
 *</p>
 *
 *@author : flyli
 *@since :2021/5/26 10
 */
class NominateViewModel(private val globalNetwork: GlobalNetwork) : ViewModel(),
    IMvvmBaseVM<IBaseView> {
    var dataList = ArrayList<HomeCommendBean.Item>()

    private var requestParamLiveData = MutableLiveData<String>()

    var nextPageUrl: String? = null

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) { url ->
        liveData {
            val result = try {
                val recommend = globalNetwork.fetchHomeRecommend(url)
                Result.success(recommend)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }

    fun onReresh() {
        Log.d("LoadRefresh", "observe: ")
        requestParamLiveData.value = MainPageService.HOME_RECOMMEND_URL
    }


    fun loadMore() {
        Log.d("LoadMore", "observe: ")
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