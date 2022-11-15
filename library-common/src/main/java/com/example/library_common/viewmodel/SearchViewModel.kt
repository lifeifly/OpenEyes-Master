package com.example.library_common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_common.search.SearchFragment
import com.example.library_network.GlobalNetwork
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import java.lang.Exception

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 12
 */
class SearchViewModel(globalNetwork: GlobalNetwork) :ViewModel(), IMvvmBaseVM<IBaseView> {
    val dataList = ArrayList<String>()

    private var requestParamLiveData = MutableLiveData<Any>()

    val dataListLiveData = Transformations.switchMap(requestParamLiveData) {
        liveData {
            val result = try {
                val hotSearch = globalNetwork.fetchHotSearch()
                Result.success(hotSearch)
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
    }


    fun onRefresh(){
        requestParamLiveData.value=requestParamLiveData.value
    }


    override fun getView(): SearchFragment? {
        return null
    }

    override fun isAttachUi(): Boolean {
        return false
    }

    override fun detachUi() {

    }

    override fun attachUi(view: IBaseView) {

    }
}