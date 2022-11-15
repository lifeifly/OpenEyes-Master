/*
 * @author flyli
 */

package com.example.module_main.moduleinit

import androidx.work.WorkManager
import com.example.library_base.base.BaseApplication
import com.example.library_common.IModuleInit
import com.example.library_common.adapter.ScreenAutoAdapter
import com.example.librery_base.global.preCreateSession
import com.example.module_main.databinding.DialogAppraiseTipsBinding
import com.example.module_main.dialog.DialogAppraiseWorker
import com.example.module_main.ui.SplashActivity
import com.example.module_main.ui.WebViewActivity


/**
 *description : <p>
 *main组件的业务初始化
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 13
 */
class MainModuleInit : IModuleInit {
    override fun onInitAhead(application: BaseApplication): Boolean {
        //初始化屏幕适配
        ScreenAutoAdapter.setUp(application)
        WebViewActivity.DEFAULT_URL.preCreateSession(application)
        if (!SplashActivity.isFirstEnterApp && DialogAppraiseWorker.isNeedShowDialog) {
            WorkManager.getInstance(application).enqueue(DialogAppraiseWorker.showDialogRequest)
        }
        return false
    }

    override fun onInitBehind(application: BaseApplication): Boolean {
        return false
    }

}