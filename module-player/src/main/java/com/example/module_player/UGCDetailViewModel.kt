package com.example.module_player

import androidx.lifecycle.ViewModel
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.viewmodel.IMvvmBaseVM

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 10
 */
class UGCDetailViewModel:ViewModel(),IMvvmBaseVM<IBaseView> {
    var dataList:List<CommunityCommedBean.Item>?=null

    var itemPosition=-1

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