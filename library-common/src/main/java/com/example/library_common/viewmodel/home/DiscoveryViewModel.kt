package com.example.library_common.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_network.GlobalNetwork
import com.example.library_network.api.MainPageService
import com.example.library_network.bean.HomeDiscoveryBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import java.lang.Exception

/**
 *description : <p>
 *首页-发现的ViewModel
 *</p>
 *
 *@author : flyli
 *@since :2021/5/29 23
 */
class DiscoveryViewModel(private val globalNetwork: GlobalNetwork) : ViewModel(),
    IMvvmBaseVM<IBaseView> {
    var nextPageUrl: String? = null//记录下一页数据

    //和发现页面的RecyclerView的适配器绑定的数据
    val dataList = ArrayList<HomeDiscoveryBean.Item>()

    //请求参数的livedata，可以被观察其改变，用于数据获取的livedata的观察
    private val requestParamLiveData = MutableLiveData<String>()

    //用于被ui界面的观察者观察
    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val data = globalNetwork.fetchHomeDiscovery(it)
                Result.success(data)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }

    fun onRefresh() {
        requestParamLiveData.value = MainPageService.HOME_DISCOVERY_URL
    }

    fun onLoad() {
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