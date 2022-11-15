package com.example.library_network.room.typeconverter

import androidx.room.TypeConverter
import com.example.library_network.bean.CommunityAttentionBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/4 11
 */
class ItemListConverter {
    @TypeConverter
    fun stringToObject(value:String):List<CommunityAttentionBean.Item>{
        val listType=object : TypeToken<List<CommunityAttentionBean.Item>>() {

        }.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter
    fun objectToString(list:List<CommunityAttentionBean.Item>):String{
        val gson=Gson()
        return gson.toJson(list)
    }
}