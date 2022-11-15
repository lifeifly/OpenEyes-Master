package com.example.librery_base.webview

import android.os.Bundle
import android.webkit.WebView
import com.tencent.sonic.sdk.SonicSessionClient
import java.util.HashMap

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/24 13
 */
class SonicSessionClientImpl: SonicSessionClient() {

    var webView:WebView?=null
    private set

    fun bindWebView(webView: WebView){
        this.webView=webView
    }

    override fun loadDataWithBaseUrlAndHeader(
        baseUrl: String?,
        data: String,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?,
        headers: HashMap<String, String>?
    ) {
        webView?.loadDataWithBaseURL(baseUrl,data,mimeType, encoding, historyUrl)
    }

    override fun loadUrl(url: String, extraData: Bundle?) {
        webView?.loadUrl(url)
    }

    override fun loadDataWithBaseUrl(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?
    ) {
        loadDataWithBaseUrl(baseUrl, data, mimeType, encoding, historyUrl)
    }

    fun destroy(){
        webView?.destroy()
        webView=null
    }

}