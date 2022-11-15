package com.example.library_network

import android.os.Build
import android.util.Log
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.screenPixel
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import kotlin.jvm.Throws

/**
 *description : <p>
 *网络配置retrofit
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 13
 */
object ServiceCreator {
    const val BASE_URL = "http://baobab.kaiyanapp.com/"

    val httpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingIntercept())
        .addInterceptor(HeaderIntercept())
        .addInterceptor(BasicParamsIntercept())
        .build()
    val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = builder.build()

    /**
     * 创建代理类对象
     * @param serviceClass Class<T>
     * @return (T..T?)
     */
    fun <T> createService(serviceClass: Class<T>) = retrofit.create(serviceClass)


    class LoggingIntercept : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            Log.d(TAG, "Sending request: ${request.url} \n ${request.headers}")
            val response = chain.proceed(request)

            val t2 = System.nanoTime()
            Log.d(
                TAG,
                "Received response for  ${response.request.url} in ${(t2 - t1) / 1e6} ms\n${response.headers}"
            )
            Log.d(TAG,"URL1")
            return response
        }

        companion object {
            const val TAG = "LoggingInterceptor"
        }
    }

    class HeaderIntercept : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val origin = chain.request()
            val request = origin.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent", System.getProperty("http.agent") ?: "unknown")
            }.build()
            return chain.proceed(request)
        }

    }

    class BasicParamsIntercept : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originRequest = chain.request()
            val originUrl = originRequest.url
            val url = originUrl.newBuilder().apply {
                addQueryParameter("udid", GlobalUtils.getDeviceSerial())
                addQueryParameter("vc", GlobalUtils.eyepetizerVersionCode.toString())
                addQueryParameter("vn", GlobalUtils.eyepetizerVersionName)
                addQueryParameter("size", screenPixel())
                addQueryParameter("deviceModel", GlobalUtils.deviceModel)
                addQueryParameter("first_channel", GlobalUtils.deviceBrand)
                addQueryParameter("last_channel", GlobalUtils.deviceBrand)
                addQueryParameter("system_version_code", "${Build.VERSION.SDK_INT}")
            }.build()
            val request =
                originRequest.newBuilder().url(url).method(originRequest.method, originRequest.body)
                    .build()
            Log.d("URL",url.toString())
            return chain.proceed(request)
        }

    }
}