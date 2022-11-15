package com.example.librery_base.utils

import android.util.Log
import com.example.librery_base.R
import com.example.librery_base.exception.ResponseCodeException
import com.example.librery_base.global.GlobalUtils
import com.google.gson.JsonSyntaxException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/28 23
 */
object ResponseHandler {
    private const val TAG="ResponseHandler"
    /**
     * 当网络请求没有正常响应的时候，根据异常类型进行相应的处理。
     * @param e 异常实体类
     */
    fun getFailureTips(e: Throwable?): String {
        Log.w(TAG, "getFailureTips exception is ${e?.message}")
        return when (e) {
            is ConnectException -> GlobalUtils.getString(R.string.network_connect_error)
            is SocketTimeoutException -> GlobalUtils.getString(R.string.network_connect_timeout)
            is ResponseCodeException -> GlobalUtils.getString(R.string.network_response_code_error) + e.responseCode
            is NoRouteToHostException -> GlobalUtils.getString(R.string.no_route_to_host)
            is UnknownHostException -> GlobalUtils.getString(R.string.network_error)
            is JsonSyntaxException -> GlobalUtils.getString(R.string.json_data_error)
            else -> {
                GlobalUtils.getString(R.string.unknown_error)
            }
        }
    }
}