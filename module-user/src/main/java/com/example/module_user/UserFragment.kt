package com.example.module_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.BuildConfig
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.librery_base.activity.IBaseView
import com.example.librery_base.fragment.MvvmLazyBaseFragemnt
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.preCreateSession
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.router.RouterFragmentPath
import com.example.librery_base.utils.ToastUtils
import com.example.librery_base.viewmodel.IMvvmBaseVM
import com.example.librery_base.webview.vassonic.SonicJavaScriptInterface
import com.example.module_user.databinding.FragmentUserLayoutBinding

/**
 *description : <p>
 *用户界面
 *</p>
 *
 *@author : flyli
 *@since :2021/5/21 00
 */

@Route(path = RouterFragmentPath.User.PAGER_USER)
class UserFragment:MvvmLazyBaseFragemnt<FragmentUserLayoutBinding,IMvvmBaseVM<IBaseView>>() {

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        viewDataBinding.tvVersionNumber.text=String.format(GlobalUtils.getString(R.string.version_show), GlobalUtils.eyepetizerVersionName)
        setOnClickListener(rootView,viewDataBinding.ivMore,viewDataBinding.ivAvatar,viewDataBinding.tvLoginTips,viewDataBinding.tvFavorite,viewDataBinding.tvCache,viewDataBinding.tvFollow,viewDataBinding.tvWatchRecord,viewDataBinding.tvNotification,viewDataBinding.tvMyBadge,viewDataBinding.tvContribute,viewDataBinding.tvFeedBakc,viewDataBinding.tvVersionNumber,viewDataBinding.llScroll){
            when(this){
                viewDataBinding.ivMore->{
                    SettingActivity.start(this.context)
                }
                viewDataBinding.ivAvatar,viewDataBinding.tvLoginTips,viewDataBinding.tvFavorite,viewDataBinding.tvCache,viewDataBinding.tvFollow,viewDataBinding.tvWatchRecord,viewDataBinding.tvNotification,viewDataBinding.tvMyBadge->{
                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                }
                viewDataBinding.tvContribute->{
                    Const.Url.AUTHOR_OPEN.preCreateSession(requireContext())
                    ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                        .withString(Const.WebViewActivity.TITLE,GlobalUtils.appName)
                        .withString(Const.WebViewActivity.LINK_URL,Const.Url.AUTHOR_OPEN)
                        .withBoolean(Const.WebViewActivity.IS_SHARE,false)
                        .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED,false)
                        .withInt(Const.WebViewActivity.PARAM_MODE,Const.WebViewActivity.MODE_SONIC)
                        .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                        .navigation()
                }
                viewDataBinding.tvFeedBakc->{
                    Const.Url.DEFAULT_URL.preCreateSession(requireContext())
                    ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                        .withString(Const.WebViewActivity.TITLE,GlobalUtils.appName)
                        .withString(Const.WebViewActivity.LINK_URL,Const.Url.DEFAULT_URL)
                        .withBoolean(Const.WebViewActivity.IS_SHARE,true)
                        .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED,false)
                        .withInt(Const.WebViewActivity.PARAM_MODE,Const.WebViewActivity.MODE_SONIC_WITH_OFFLINE_CACHE)
                        .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                        .navigation()
                }
                viewDataBinding.tvVersionNumber->{
                    Const.Url.DEFAULT_URL.preCreateSession(requireContext())
                    ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                        .withString(Const.WebViewActivity.TITLE,GlobalUtils.appName)
                        .withString(Const.WebViewActivity.LINK_URL,Const.Url.DEFAULT_URL)
                        .withBoolean(Const.WebViewActivity.IS_SHARE,true)
                        .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED,false)
                        .withInt(Const.WebViewActivity.PARAM_MODE,Const.WebViewActivity.MODE_SONIC_WITH_OFFLINE_CACHE)
                        .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                        .navigation()
                }
                this@UserFragment.rootView,viewDataBinding.llScroll->{
                    AboutActivity.start(this.context)
                }
                else->{}
            }

        }
        viewDataBinding.tvVersionNumber.setOnLongClickListener {
            val channel = String.format(GlobalUtils.getString(R.string.channel), GlobalUtils.getApplicationMetaData("UMENG_CHANNEL"))
            val buildType = String.format(GlobalUtils.getString(R.string.build_type), BuildConfig.BUILD_TYPE)
            val versionName = String.format(GlobalUtils.getString(R.string.version_name), GlobalUtils.appVersionName)
            ToastUtils.show("${channel}\n${buildType}\n${versionName}")
            true
        }
    }


    override fun getVariable(): Int {
        return 0
    }

    override fun getViewModel(): IMvvmBaseVM<IBaseView>? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_layout
    }

    override fun onRetryCallback() {

    }

}