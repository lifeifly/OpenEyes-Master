package com.example.library_common

import android.app.Application
import android.content.Context
import android.util.Log
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/3 14
 */
class UMPushHelper {

    companion object{
        const val APP_KEY="60b864e46c421a3d97daf775"
        const val UMENG_MESSAGE_SECRET="5ef49415e9f2b1d954ca0e634c8697f7"
        const val APP_MASTER_SECRET="ktpkrd28vg8kzkdcvompgnwislhruo9b"
        const val CHANNEL="Umeng"

        fun preInit(application:Application){
            UMConfigure.setLogEnabled(true)


        }

        fun init(context: Context){
            Log.i("TAG0", "注册成功：deviceToken：--> " );
            UMConfigure.init(
                context,
                APP_KEY,
                CHANNEL,
                UMConfigure.DEVICE_TYPE_PHONE,
                UMENG_MESSAGE_SECRET
            )

            //获取消息推送实例
            val pushAgent=PushAgent.getInstance(context)
            //注册推送服务，每次调用register方法都会回调该接口
            pushAgent.register(object : IUmengRegisterCallback{
                override fun onSuccess(deviceToken: String?) {
                    //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                    Log.i("TAG2", "注册成功：deviceToken：--> " + deviceToken);
                }

                override fun onFailure(p0: String?, p1: String?) {
                    Log.i("TAG3", "注册失败：--> " + "s:" + p0 + ",s1:" + p1);
                }
            })
        }
    }
}