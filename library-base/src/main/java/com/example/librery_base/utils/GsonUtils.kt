package com.example.librery_base.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

/**
 *description : <p>
 *json解析工具类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 23
 */
object GsonUtils {
    //时间格式字符串
    const val DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ssZ"
    //本地化
    private val mLocalGson=createLocalGson()


    private val mRemoteGson=createRemoteGson()

    private fun createLocalGsonBuilder(): GsonBuilder {
        val gsonBuilder=GsonBuilder()
        gsonBuilder.setLenient()
        gsonBuilder.setDateFormat(DATE_FORMAT)
        return gsonBuilder
    }

    private fun createLocalGson(): Gson {
        return createLocalGsonBuilder().create()
    }


    private fun createRemoteGson(): Gson {
        return createLocalGsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    fun getLocalGson():Gson{
        return mLocalGson
    }

    /**
     * 反序列化
     * @param json String
     * @param clazz Class<T>
     * @return T
     */
    fun <T> fromLocalGson(json:String,clazz: Class<T>):T{
        return mLocalGson.fromJson(json,clazz)
    }

    fun <T> fromLocalGson(json: String,typeOfT:Type):T{
        return mLocalGson.fromJson(json,typeOfT)
    }
    /**
     * 序列化
     */
    fun toJson(src:Any):String{
        return mLocalGson.toJson(src)
    }

    fun toRemoteJson(src: Any):String{
        return mRemoteGson.toJson(src)
    }
}