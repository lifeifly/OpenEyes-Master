package com.example.module_main.ui

import android.content.Intent
import android.os.Bundle
import android.os.Trace
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.TraceCompat
import com.example.library_common.adapter.ScreenAutoAdapter
import com.example.librery_base.utils.DataStoreUitls

import com.example.librery_base.utils.ToastUtils
import com.example.module_main.R
import com.example.module_main.databinding.ActivitySplashBinding
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umeng.message.PushAgent
import io.reactivex.functions.Consumer
import kotlinx.coroutines.*

/**
 *description :
 *主业务模块
 *欢迎页面
 *@author : flyli
 *@since :
 *
 */
class SplashActivity : AppCompatActivity() {

    var _binding:ActivitySplashBinding?=null

    private val binding:ActivitySplashBinding
        get() = _binding!!
    //需要执行的协程任务
    private val job by lazy { Job() }

    //闪屏时间
    private val SPLASH_DURATION = 3 * 1000L

    //透明度动画
    private val alphaAnimation by lazy {
        AlphaAnimation(0.5F, 1.0F).apply {
            duration = SPLASH_DURATION
            fillAfter = true
        }
    }

    //缩放动画
    private val scaleAnimation by lazy {
        ScaleAnimation(
            1F, 1.05F, 1F, 1.05F, Animation.RELATIVE_TO_SELF,
            0.5F, Animation.RELATIVE_TO_SELF, 0.5F
        ).apply {
            duration = SPLASH_DURATION
            fillAfter = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //适配屏幕
        ScreenAutoAdapter.match(this, 375)
        _binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        PushAgent.getInstance(this).onAppStart()
//        //实现沉浸式
        ImmersionBar.with(this)
            .hideBar(BarHide.FLAG_HIDE_BAR)//隐藏状态栏和导航栏
            .init()
        //执行动画
        binding.splashIvBg.startAnimation(scaleAnimation)
        binding.splashIvSlogan.startAnimation(alphaAnimation)
        //用协程延迟3秒启动MainActivity，不会阻塞主线程
        CoroutineScope(job).launch(Dispatchers.Main) {
            delay(SPLASH_DURATION)
            //执行权限申请
            val permission = RxPermissions(this@SplashActivity)
            permission.request(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.RECEIVE_BOOT_COMPLETED
            )
                .subscribe(object : Consumer<Boolean> {
                    override fun accept(t: Boolean?) {
                        if (t != null && t) {
                            //申请成功，开始进入主界面
                            startToMain()
                        } else {
                            ToastUtils.show("未赋予权限，不能正常使用")
                            finish()
                        }
                    }
                })
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        Log.i("AppApplication","onWindowFocusChanged"+Thread.currentThread())
        Trace.endSection()
        super.onWindowFocusChanged(hasFocus)

    }

    //跳转到MainActivity
    private fun startToMain() {
        MainActivity.start(this)
        isFirstEnterApp=false
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消协程任务
        job.cancel()
    }
    companion object {
        /**
         * 是否首次进入App
         */
        var isFirstEnterApp: Boolean
            get() = DataStoreUitls.readBooleanData("is_first_enter_app")
            set(value) {
                CoroutineScope(Dispatchers.IO).launch {
                    DataStoreUitls.putDataAsync("is_first_enter_app", value)
                }
            }
    }
}