package com.example.module_main.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_common.Const
import com.example.library_common.Const.WebViewActivity.Companion.IS_SHARE
import com.example.library_common.Const.WebViewActivity.Companion.IS_TITLE_FIXED
import com.example.library_common.Const.WebViewActivity.Companion.LINK_URL
import com.example.library_common.Const.WebViewActivity.Companion.MODE_DEFAULT
import com.example.library_common.Const.WebViewActivity.Companion.MODE_SONIC
import com.example.library_common.Const.WebViewActivity.Companion.MODE_SONIC_WITH_OFFLINE_CACHE
import com.example.library_common.Const.WebViewActivity.Companion.PARAM_MODE
import com.example.library_common.Const.WebViewActivity.Companion.TITLE
import com.example.library_common.R
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.preCreateSession
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ShareUtils
import com.example.module_main.databinding.ActivityWebViewBinding
import com.example.librery_base.webview.SonicSessionClientImpl
import com.example.librery_base.webview.vassonic.OfflinePkgSessionConnection
import com.example.librery_base.webview.vassonic.SonicJavaScriptInterface
import com.example.librery_base.webview.vassonic.SonicRuntimeImpl
import com.gyf.immersionbar.ImmersionBar
import com.tencent.sonic.sdk.*
import com.umeng.message.PushAgent

/**
 * 展示网页共同给界面
 */
@Route(path = RouterActivityPath.WebView.PAGER_WEBVIEW)
class WebViewActivity : AppCompatActivity() {
    var _binding: ActivityWebViewBinding? = null

    val binding: ActivityWebViewBinding
        get() = _binding!!

    private var title: String = ""

    private var linkUrl: String = ""

    private var isShare: Boolean = false

    private var isTitleFixed: Boolean = false

    private var sonicSession: SonicSession? = null

    private var sonicSessionClient: SonicSessionClientImpl? = null

    private var mode: Int = MODE_DEFAULT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWebViewBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        PushAgent.getInstance(this).onAppStart()

        initParams()
        initView()
        preLoadInitVasSonic()

    }

    private fun initView() {
        initTitleBar()
        initWebView()
        if (sonicSessionClient != null) {
            sonicSessionClient?.bindWebView(binding.webview)
            sonicSessionClient?.clientReady()
        } else {
            binding.webview.loadUrl(linkUrl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.webview.settings.run {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            javaScriptEnabled = true
            binding.webview.removeJavascriptInterface("searchBoxJavaBridge_")
            intent.putExtra(
                SonicJavaScriptInterface.PARAM_LOAD_URL_TIME,
                System.currentTimeMillis()
            )
            binding.webview.addJavascriptInterface(
                SonicJavaScriptInterface(
                    sonicSessionClient,
                    intent
                ), "sonic"
            )
            allowContentAccess = true
            databaseEnabled = true
            domStorageEnabled = true
            setAppCacheEnabled(true)
            savePassword = false
            saveFormData = false
            useWideViewPort = true
            loadWithOverviewMode = true
            defaultTextEncodingName = "UTF-8"
            setSupportZoom(true)
        }
        binding.webview.webChromeClient = UIWebChromeClient()
        binding.webview.webViewClient = UIWebViewClient()
        binding.webview.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            //调用系统浏览器下载
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun initTitleBar() {
        ImmersionBar.with(this)
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(R.color.common_color_bar)
            .fitsSystemWindows(true)
            .init()
        binding.titleBar2.tvTitle.text = title
        binding.titleBar2.ivBack.setOnClickListener{v ->
            finish()
        }
        binding.titleBar2.tvTitle.isSelected=true//实现跑马灯效果
        if (isShare) binding.titleBar2.ivShare.isVisible = true
        binding.titleBar2.ivShare.setOnClickListener { showDialogShare("${title}:${linkUrl}") }
    }

    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            finish()
        }
    }

    /**
     * 使用VasSonic框架提升H5收评加载速度
     */
    private fun preLoadInitVasSonic() {
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)

        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(application), SonicConfig.Builder().build())
        }

        if (MODE_DEFAULT != mode) {
            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            if (MODE_SONIC_WITH_OFFLINE_CACHE == mode) {
                sessionConfigBuilder.setCacheInterceptor(object : SonicCacheInterceptor(null) {
                    override fun getCacheData(session: SonicSession?): String? {
                        return null
                    }
                })
                sessionConfigBuilder.setConnectionInterceptor(object :
                    SonicSessionConnectionInterceptor() {
                    override fun getConnection(
                        session: SonicSession?,
                        intent: Intent?
                    ): SonicSessionConnection {
                        return OfflinePkgSessionConnection(this@WebViewActivity, session, intent)
                    }
                })
            }

            sonicSession=SonicEngine.getInstance().createSession(linkUrl,sessionConfigBuilder.build())
            if (sonicSession!=null){
                sonicSession?.bindClient(SonicSessionClientImpl().also { sonicSessionClientImpl ->
                    sonicSessionClient=sonicSessionClientImpl
                })
            }else{
                Log.d(TAG, "${title},${linkUrl}:create sonic session fail!")
            }
        }
    }

    private fun initParams() {
        title = intent.getStringExtra(TITLE) ?: GlobalUtils.appName
        linkUrl = intent.getStringExtra(LINK_URL) ?: DEFAULT_URL
        isShare = intent.getBooleanExtra(IS_SHARE, false)
        isTitleFixed = intent.getBooleanExtra(IS_TITLE_FIXED, false)
        mode = intent.getIntExtra(PARAM_MODE, MODE_DEFAULT)
    }

    override fun onDestroy() {
        binding.webview.destroy()
        sonicSession?.destroy()
        sonicSession = null
        _binding = null
        super.onDestroy()
    }

    inner class UIWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
            Log.d(TAG, "onPageStarted >>> url:${url}")
            linkUrl = url
            super.onPageStarted(view, url, favicon)
            binding.progressBar.visibility = View.VISIBLE

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Log.d(TAG, "onPageFinished >>> url:${url}")
            super.onPageFinished(view, url)
            sonicSession?.sessionClient?.pageFinish(url)
            binding.progressBar.visibility = View.INVISIBLE
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            if (sonicSession != null) {
                val requestResponse = sonicSessionClient?.requestResource(linkUrl)
                if (requestResponse is WebResourceResponse) return requestResponse
            }
            return null
        }
    }

    inner class UIWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            Log.d(TAG, "onReceivedTitle >>> title:${title}")
            if (!isTitleFixed) {
                title?.run {
                    this@WebViewActivity.title = this
                    binding.titleBar2.tvTitle.text = this

                }
            }
        }
    }

    companion object {
        const val TAG = "WebViewActivity"
        const val DEFAULT_URL = "https://github.com/VIPyinzhiwei/Eyepetizer"
        val DEFAULT_TITLE = GlobalUtils.appName

        /**
         * 跳转WebView网页界面
         *  @param context       上下文环境
         * @param title         标题
         * @param url           加载地址
         * @param isShare       是否显示分享按钮
         * @param isTitleFixed  是否固定显示标题，不会通过动态加载后的网页标题而改变。true：固定，false 反之。
         * @param mode          加载模式：MODE_DEFAULT 默认使用WebView加载；MODE_SONIC 使用VasSonic框架加载； MODE_SONIC_WITH_OFFLINE_CACHE 使用VasSonic框架离线加载
         */
        fun start(
            context: Context, title: String, url: String, isShare: Boolean = true,
            isTitleFixed: Boolean = true, mode: Int = MODE_SONIC
        ) {
            url.preCreateSession(context)//预加载url
            val intent = Intent(context, WebViewActivity::class.java).apply {
                putExtra(Const.WebViewActivity.TITLE, title)
                putExtra(LINK_URL, url)
                putExtra(IS_SHARE, isShare)
                putExtra(IS_TITLE_FIXED, isTitleFixed)
                putExtra(PARAM_MODE, mode)
                putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis())
            }
            context.startActivity(intent)
        }
    }

    /**
     * 调用系统原生分享
     *
     * @param shareContent 分享内容
     * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
     */
    fun share(shareContent: String, shareType: Int) {
        ShareUtils.share(this, shareContent, shareType)
    }

    /**
     * 弹出分享对话框
     *
     * @param shareContent 分享内容
     */
    fun showDialogShare(shareContent: String) {
        com.example.librery_base.global.showDialogShare(this, shareContent)
    }
}