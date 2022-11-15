package com.example.library_network.room.typeconverter

import androidx.room.TypeConverter
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

class ListUrlConverter {
    @TypeConverter
    fun stringToObejct(value:String):List<Url>{
        val type=object : TypeToken<List<Url>>(){}.type
        return Gson().fromJson(value,type)
    }

    @TypeConverter
    fun objectToString(list:List<Url>):String{
        val gson=Gson()
        return gson.toJson(list)
    }
}