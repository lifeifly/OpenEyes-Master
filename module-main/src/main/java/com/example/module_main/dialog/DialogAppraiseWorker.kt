package com.example.module_main.dialog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.library_common.Const.WebViewActivity.Companion.MODE_SONIC_WITH_OFFLINE_CACHE
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.librery_base.global.screenWidth
import com.example.librery_base.utils.DataStoreUitls
import com.example.module_main.R
import com.example.module_main.ui.WebViewActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 *description : <p>
 *WorkManager组件，执行后台弹出框任务
 *</p>
 *
 *@author : flyli
 *@since :2021/5/23 19
 */
class DialogAppraiseWorker(private val context: Context,private val params: WorkerParameters):Worker(context,params) {
    //执行的任务
    override fun doWork(): Result {
        return if (isNeedShowDialog) {
            Log.d(TAG, "${System.currentTimeMillis()},execute doWork function,result : retry")
            Result.retry()
        } else {
            Log.d(TAG, "${System.currentTimeMillis()},execute doWork function,result : success")
            Result.success()
        }
    }

    companion object{
        const val TAG="DialogAppraiseWorker"

        val showDialogRequest=OneTimeWorkRequest.Builder(DialogAppraiseWorker::class.java)
            .addTag(TAG)
            .setInitialDelay(1,TimeUnit.MINUTES)
            .setBackoffCriteria(BackoffPolicy.LINEAR,5,TimeUnit.SECONDS)
            .build()

        /**
         * 是否需要弹出对话框
         */
        var isNeedShowDialog:Boolean
        get() = DataStoreUitls.readBooleanData("is_need_show_dialog",true)
        set(value) {
            CoroutineScope(Dispatchers.IO).launch { DataStoreUitls.saveBooleanDataAsync("is_need_show_dialog",value) }
        }

        private var dialog:AlertDialog?=null

        fun showDialog(context: Context){
            if (!isNeedShowDialog)return

            val dialogView=LayoutInflater.from(context).inflate(R.layout.dialog_appraise_tips,null).apply {
                findViewById<TextView>(R.id.tv_des).text=String.format(GlobalUtils.getString(R.string.encourge),GlobalUtils.appName)
                findViewById<TextView>(R.id.tv_notencourage).setOnClickListener { v ->
                    dialog?.dismiss()
                }
                findViewById<TextView>(R.id.tv_encourage).setOnClickListener { v ->
                    dialog?.dismiss()
                    WebViewActivity.start(context,WebViewActivity.DEFAULT_TITLE, WebViewActivity.DEFAULT_URL, true, false, MODE_SONIC_WITH_OFFLINE_CACHE)
                }
            }
            dialog=AlertDialog.Builder(context,R.style.EyepetizerAlertDialogStyle)
                .setCancelable(false)
                .setView(dialogView)
                .create()
            dialog?.show()
            dialog?.window?.attributes= dialog?.window?.attributes?.apply {
                width= screenWidth-(dp2px(20F)*2)
                height=ViewGroup.LayoutParams.WRAP_CONTENT
            }
            isNeedShowDialog=false

        }
    }
}