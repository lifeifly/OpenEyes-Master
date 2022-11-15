package com.example.library_common.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_network.GlobalNetwork
import com.example.library_network.api.MainPageService
import com.example.library_network.bean.HomeDailyBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import java.lang.Exception

/**
 *description : <p>
 *首页-日报的ViewModel
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 18
 */
class DailyViewModel(private val globalNetwork: GlobalNetwork) : ViewModel(),
    IMvvmBaseVM<IBaseView> {
     var nextPage: String? = null

    //供外部ui绑定的数据
    val dataList = ArrayList<HomeDailyBean.Item>()

    //供数据持有的LiveData观察的持有请求参数的LiveData
    val requestParamLiveData = MutableLiveData<String>()

    //持有数据的LiveData
    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val data = globalNetwork.fetchHomeDaily(it)
                Result.success(data)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }


    fun onRefresh() {
        requestParamLiveData.value = MainPageService.HOME_DAILY_URL
    }

    fun onLoadMore() {
        requestParamLiveData.value = nextPage ?: ""
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