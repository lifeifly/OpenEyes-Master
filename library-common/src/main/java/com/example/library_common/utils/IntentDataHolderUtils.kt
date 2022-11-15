package com.example.library_common.utils

import java.lang.Exception
import java.lang.ref.WeakReference

/**
 *description : <p>
 *数据传输工具类，处理Intent携带大量数据时，超过1MB限制出现异常的场景
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 12
 */
object IntentDataHolderUtils {
    private val dataList = hashMapOf<String, WeakReference<Any>>()

    fun setData(key: String, t: Any) {
        val value = WeakReference(t)
        dataList[key] = value
    }

    fun <T> getData(key: String): T? {
        val reference = dataList[key]
        return try {
            reference?.get() as T
        } catch (e: Exception) {
            null
        }
    }
}