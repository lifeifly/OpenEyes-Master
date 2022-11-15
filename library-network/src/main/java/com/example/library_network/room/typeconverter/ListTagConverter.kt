package com.example.library_network.room.typeconverter

import androidx.room.TypeConverter
import com.example.library_network.bean.Tag
import com.example.library_network.bean.Url
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/4 12
 */

class ListTagConverter {
    @TypeConverter
    fun stringToObejct(value:String):List<Tag>{
        val type=object : TypeToken<List<Tag>>(){}.type
        return Gson().fromJson(value,type)
    }

    @TypeConverter
    fun objectToString(list:List<Tag>):String{
        val gson=Gson()
        return gson.toJson(list)
    }
}