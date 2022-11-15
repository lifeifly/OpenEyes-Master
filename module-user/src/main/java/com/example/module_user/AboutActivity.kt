package com.example.module_user

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.preCreateSession
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.webview.vassonic.SonicJavaScriptInterface
import com.example.module_user.databinding.ActivityAboutBinding
import com.gyf.immersionbar.ImmersionBar
import com.umeng.message.PushAgent

class AboutActivity : AppCompatActivity() {
    var _binding:ActivityAboutBinding?=null

    private val binding:ActivityAboutBinding
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PushAgent.getInstance(this).onAppStart()
        _binding= ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ImmersionBar.with(this)
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(R.color.colorPrimaryDark)
            .fitsSystemWindows(true).init()
        binding.titleBar.tvTitle.text = GlobalUtils.getString(R.string.about)
        val version = "${GlobalUtils.getString(R.string.version)} ${GlobalUtils.appVersionName}"
        binding.tvAboutVersion.text = version
        binding.tvThanksTips.text = String.format(GlobalUtils.getString(R.string.thanks_to), GlobalUtils.appName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvOpenSourceList.text = Html.fromHtml("<u>" + GlobalUtils.getString(R.string.open_source_project_list) + "</u>", 0)
        } else {
            binding.tvOpenSourceList.text = Html.fromHtml("<u>" + GlobalUtils.getString(R.string.open_source_project_list) + "</u>")
        }
        binding.ivLogo.setImageDrawable(GlobalUtils.getAppIcon())

        binding.btnEncourage.setOnClickListener {
            Const.Url.DEFAULT_URL.preCreateSession(this)
            ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                .withString(Const.WebViewActivity.TITLE,GlobalUtils.appName)
                .withString(Const.WebViewActivity.LINK_URL,Const.Url.DEFAULT_URL)
                .withBoolean(Const.WebViewActivity.IS_SHARE,true)
                .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED,true)
                .withInt(Const.WebViewActivity.PARAM_MODE,Const.WebViewActivity.MODE_SONIC)
                .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                .navigation()
        }
        binding.tvOpenSourceList.setOnClickListener {
            OpenSourceProjectActivity.start(this@AboutActivity)
        }
        binding.titleBar.ivBack.setOnClickListener {
            finish()
        }
    }

    companion object{
        fun start(context: Context){
            val intent= Intent(context,AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}