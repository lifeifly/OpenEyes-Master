package com.example.module_user

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.example.library_common.Const
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.preCreateSession
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.DataStoreUitls
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.librery_base.webview.vassonic.SonicJavaScriptInterface
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.tencent.sonic.sdk.SonicEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 23
 */
class SettingViewModel : ViewModel(), IMvvmBaseVM<IBaseView> {
    //日报更新提醒
    var rbDaily: Boolean
        get() = DataStoreUitls.readBooleanData("dailyOnOff", true)
        set(value) = DataStoreUitls.saveBooleanDataSync("dailyOnOff", value)

    //wifi 关注页自动播放提醒
    var rbWifiOpen: Boolean
        get() = DataStoreUitls.readBooleanData("wifiOnOff", true)
        set(value) = DataStoreUitls.saveBooleanDataSync("wifiOnOff", false)

    //翻译
    var rbTranslationOPen: Boolean
        get() = DataStoreUitls.readBooleanData("translateOnOff", true)
        set(value) = DataStoreUitls.saveBooleanDataSync("translateOnOff", value)

    fun onClick(view: View) {
        when (view.id) {
            R.id.tvClearCache -> {
                clearCache(view.context)
            }
            R.id.tvOptionCachePath, R.id.tvOptionDefinition, R.id.tvOptionCacheDefinition -> {
                ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN)
                    .navigation()
            }
            R.id.tvCheckVersion -> {
                ToastUtils.show(R.string.currently_not_supported)
            }
            R.id.tvUserAgreement -> {
                Const.Url.USER_AGREEMENT.preCreateSession(view.context)
                ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                    .withString(Const.WebViewActivity.TITLE, GlobalUtils.appName)
                    .withString(Const.WebViewActivity.LINK_URL, Const.Url.USER_AGREEMENT)
                    .withBoolean(Const.WebViewActivity.IS_SHARE, false)
                    .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED, false)
                    .withInt(Const.WebViewActivity.PARAM_MODE, Const.WebViewActivity.MODE_SONIC)
                    .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                    .navigation()
            }
            R.id.tvLegalNotice -> {
                Const.Url.LEGAL_NOTICES.preCreateSession(view.context)
                ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                    .withString(Const.WebViewActivity.TITLE, GlobalUtils.appName)
                    .withString(Const.WebViewActivity.LINK_URL, Const.Url.LEGAL_NOTICES)
                    .withBoolean(Const.WebViewActivity.IS_SHARE, false)
                    .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED, false)
                    .withInt(Const.WebViewActivity.PARAM_MODE, Const.WebViewActivity.MODE_SONIC)
                    .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                    .navigation()
            }
            R.id.tvVideoFunStatement, R.id.tvCopyrightReport -> {
                Const.Url.VIDEO_FUNCTION_STATEMENT.preCreateSession(view.context)
                ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                    .withString(Const.WebViewActivity.TITLE, GlobalUtils.appName)
                    .withString(Const.WebViewActivity.LINK_URL, Const.Url.VIDEO_FUNCTION_STATEMENT)
                    .withBoolean(Const.WebViewActivity.IS_SHARE, false)
                    .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED, false)
                    .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                    .withInt(Const.WebViewActivity.PARAM_MODE, Const.WebViewActivity.MODE_SONIC)
                    .navigation()
            }
            R.id.tvSlogan, R.id.tvDescription -> {
                Const.Url.DEFAULT_URL.preCreateSession(view.context)
                ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                    .withString(Const.WebViewActivity.TITLE, GlobalUtils.appName)
                    .withString(Const.WebViewActivity.LINK_URL, Const.Url.DEFAULT_URL)
                    .withBoolean(Const.WebViewActivity.IS_SHARE, true)
                    .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED, false)
                    .withInt(
                        Const.WebViewActivity.PARAM_MODE,
                        Const.WebViewActivity.MODE_SONIC_WITH_OFFLINE_CACHE
                    )
                    .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                    .navigation()
            }
            R.id.llScroll -> {
                AboutActivity.start(view.context)
            }
            else -> {
            }
        }
    }

    private fun clearCache(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            //清楚缓存
            GSYVideoManager.instance().clearAllDefaultCache(context)
            Glide.get(context).clearMemory()
            withContext(Dispatchers.IO) {
                Glide.get(context).clearDiskCache()
                if (SonicEngine.isGetInstanceAllowed()) {
                    SonicEngine.getInstance().cleanCache()
                }
            }
            ToastUtils.show(R.string.clear_cache_succeed)
        }
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