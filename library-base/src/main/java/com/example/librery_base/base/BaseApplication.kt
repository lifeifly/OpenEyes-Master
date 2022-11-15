/*
 * @author flyli
 */

package com.example.library_base.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle


/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/18 22
 */
open class BaseApplication: Application() {
    companion object {
        private var mInstance: BaseApplication? = null

        private var mIsDebug: Boolean = false

        /**
         * 获取实例
         * @return BaseApplication
         */
        fun getInstance(): BaseApplication {
            if (mInstance == null) {
                throw NullPointerException("mInstance is null,please call setApplication")
            }
            return mInstance!!
        }

        /**
         * 获取进程名
         * @param context Context
         * @return String
         */
        fun getCurrentProgress(context: Context):String?{
            val am=context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningApps=am.runningAppProcesses
            if (runningApps==null){
                return null
            }
            for (proInfo in runningApps){
                if (proInfo.pid ==android.os.Process.myPid()){
                    if (proInfo.processName!=null){
                        return proInfo.processName
                    }
                }
            }
            return null
        }
    }

    override fun onCreate() {
        super.onCreate()
        setApplication(this)
    }

    /**
     * 当宿主application没有继承该application的时候，使用set方法进行初始化baseApplication
     * @param baseApplication BaseApplication
     */
    private fun setApplication(baseApplication: BaseApplication) {
        mInstance = baseApplication
        baseApplication.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                AppManager.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                AppManager.removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

        })
    }

    fun setDebug(isDebug:Boolean){
        mIsDebug=isDebug
    }

    fun getDebug():Boolean{
        return mIsDebug
    }


}