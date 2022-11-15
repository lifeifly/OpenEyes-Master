/*
 * @author flyli
 */

package com.example.android_mvvmcomponent

import android.content.Context
import android.os.Trace
import android.util.Log
import androidx.core.os.TraceCompat
import androidx.databinding.library.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_base.base.BaseApplication
import com.example.library_common.config.ModuleLifecycleConfig

/**
 *description : <p>
 *宿主app的application，在这里通过common组件中定义的组件生命周期配置类进行初始化(通过反射调用各个组件的初始化器)
 *</p>
 *
 *@author : flyli
 *@since :2021/5/18 23
 */
class AppApplication : BaseApplication() {

    override fun attachBaseContext(base: Context?) {
        Log.i("AppApplication","attachBaseContext"+Thread.currentThread())
        Trace.beginSection("SplashStart")
        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        //设置是否处于debug状态
        setDebug(BuildConfig.DEBUG)
        //初始化需要初始化的组件
        ModuleLifecycleConfig.initModuleAhead(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        //销毁arouter
        ARouter.getInstance().destroy()
    }
}