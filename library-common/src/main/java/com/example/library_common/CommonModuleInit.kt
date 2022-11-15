package com.example.library_common

import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_base.base.BaseApplication
import com.example.library_common.recyclerview.NoStatusFooter
import com.example.librery_base.global.GlobalUtils
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


/**
 *description : <p>
 *通用库common 基础库 base 自身初始化
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 22
 */
class CommonModuleInit : IModuleInit {

    override fun onInitAhead(application: BaseApplication): Boolean {
        //初始化ARouter
        if (application.getDebug()) {
            ARouter.openLog()//开启日志
            ARouter.openDebug()// 使用InstantRun的时候，需要打开该开关，上线之后关闭，否则有安全风险
        }
        ARouter.init(application)
        //初始化GloBalUtils
        GlobalUtils.init(application.applicationContext)
        //初始化SamrtRefreshLayout
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableLoadMore(true)//开启加载更多
            layout.setEnableLoadMoreWhenContentNotFull(true)//设置在内容不满一页的时候，是否可以上拉加载更多
        }
        //设置刷新头
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)//设置下拉刷新时内容随头部移动
            MaterialHeader(context).setColorSchemeResources(
                R.color.blue,
                R.color.blue,
                R.color.blue
            )//设置谷歌刷新头
        }
        //设置刷新底部
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)//设置是否在没有更多数据之后 Footer 跟随内容
            layout.setEnableFooterTranslationContent(true)//设置是否启在上拉 Footer 的同时上拉内容
            layout.setFooterHeight(153F)//设置底部高度
            layout.setFooterTriggerRate(0.6F)//设置 触发加载距离 与 FooterHeight 的比率
            NoStatusFooter.REFRESH_FOOTER_NOTHING=GlobalUtils.getString(R.string.footer_not_more)
            NoStatusFooter(context).apply {
                setAccentColorId(R.color.colorTextPrimary)
                setTextTitleSize(16F)
            }
        }

        UMPushHelper.preInit(application)
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                UMPushHelper.init(application)
            }
        }
        return false
    }

    override fun onInitBehind(application: BaseApplication): Boolean {
        return false
    }
}