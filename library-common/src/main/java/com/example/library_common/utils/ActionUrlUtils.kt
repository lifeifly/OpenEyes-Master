package com.example.library_common.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.R
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.eventbus.SwitchPagesEvent
import com.example.librery_base.global.preCreateSession
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.webview.vassonic.SonicJavaScriptInterface
import org.greenrobot.eventbus.EventBus
import java.net.URLDecoder

/**
 *description : <p>
 *通过截取ActionUrl进行处理
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 12
 */
object ActionUrlUtils {
    /**
     * 处理actionurl事件
     * @param fragment Fragment上下文环境
     * @param actionUrl String? 待处理的url
     * @param toastTitle String toast标题
     */
    fun process(fragment: Fragment, actionUrl: String?, toastTitle: String = "") {
        process(fragment.activity, actionUrl, toastTitle)
    }

    /**
     * 处理actionurl事件
     * @param activity Activity 上下文环境
     * @param actionUrl String? 待处理的url
     * @param toastTitle String toast标题
     */
    fun process(activity: Activity?, actionUrl: String?, toastTitle: String = "") {
        if (actionUrl == null) return
        val decodeUrl = URLDecoder.decode(actionUrl, "UTF-8")

        when {
            decodeUrl.startsWith(Const.ActionUrl.WEBVIEW) -> {
                //预加载url
                val data = decodeUrl.getWebViewInfo()
                data.last().preCreateSession(activity as Context)
                //跳转到WebViewActivity
                ARouter.getInstance()
                    .build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                    .withString(Const.WebViewActivity.TITLE, data.first())
                    .withString(Const.WebViewActivity.LINK_URL, data.last())
                    .withBoolean(Const.WebViewActivity.IS_SHARE,true)
                    .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED,true)
                    .withInt(Const.WebViewActivity.PARAM_MODE,Const.WebViewActivity.MODE_SONIC)
                    .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                    .navigation()
            }
            decodeUrl.startsWith(Const.ActionUrl.RANKLIST) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.TAG) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.HP_SEL_TAB_TWO_NEWTAB_MINUS_THREE) -> {
                //选择日报fragment
                EventBus.getDefault().post(SwitchPagesEvent(Navigation.Home.HOME_DAILY))
            }
            decodeUrl.startsWith(Const.ActionUrl.CM_TAGSQUARE_TAB_ZERO) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.CM_TOPIC_SQUARE) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.CM_TOPIC_SQUARE_TAB_ZERO) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.COMMON_TITLE) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.HP_NOTIFI_TAB_ZERO) -> {
                //跳转到通知 推荐列表
                EventBus.getDefault().post(SwitchPagesEvent(Navigation.Notification.NOTIFICATION_PUSH))
                EventBus.getDefault().post(RefreshEvent(Navigation.Notification.NOTIFICATION_PUSH))
            }
            decodeUrl.startsWith(Const.ActionUrl.TOPIC_DETAIL) -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            decodeUrl.startsWith(Const.ActionUrl.DETAIL) -> {
                getConversionVideoId(actionUrl)?.run {
                    Log.d("EVENTBUS", "process:2 "+actionUrl)
                    //跳转到newdetailactivity
                    ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                        .withLong(Const.ExtraTag.EXTRA_VIDEO_ID, this)
                        .navigation()
                }
            }
            else -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
        }
    }
}

/**
 * 截取标题与url信息。
 *
 * @return first=标题 last=url
 */
private fun String.getWebViewInfo(): Array<String> {
    val title = this.split("title=").last().split("&url").first()
    val url = this.split("url=").last()
    return arrayOf(title, url)
}

/**
 *  截取视频id。
 *
 *  @param actionUrl 解码后的actionUrl
 *  @return 视频id
 */
private fun getConversionVideoId(actionUrl: String?): Long? {
    return try {
        val list = actionUrl?.split(Const.ActionUrl.DETAIL)
        list!![1].toLong()
    } catch (e: Exception) {
        null
    }
}