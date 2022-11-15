package com.example.module_main.utils

import android.content.Context
import androidx.core.content.ContextCompat

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 21
 */
class ColorUtils {
    companion object{
        fun getColor(context: Context,colorId:Int):Int{
            return ContextCompat.getColor(context,colorId)
        }
    }
}