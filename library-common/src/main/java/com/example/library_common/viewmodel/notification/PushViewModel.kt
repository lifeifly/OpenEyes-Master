package com.example.library_common.viewmodel.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.library_network.GlobalNetwork
import com.example.library_network.api.MainPageService
import com.example.library_network.bean.NotificationPushBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM
import java.lang.Exception

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/2 16
 */
class PushViewModel(private val globalNetwork: GlobalNetwork):ViewModel(),IMvvmBaseVM<IBaseView> {
    var dataList=ArrayList<NotificationPushBean.Message>()

    private var requestParamLiveData=MutableLiveData<String>()

    var nextPage:String?=null

    val dataListLiveData=Transformations.switchMap(requestParamLiveData){
        liveData {
            val result=try {
                val data=globalNetwork.fetchNotificationPush(it)
                Result.success(data)
            }catch (e:Exception){
                Result.failure(e)
            }
            emit(result)
        }
    }

    fun onRefresh(){
        requestParamLiveData.value=MainPageService.NOTIFICATION_PUSH_URL
    }
    fun onLoadMore(){
        requestParamLiveData.value=nextPage?:""
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