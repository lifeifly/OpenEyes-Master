package com.example.librery_base.extension

import android.view.View

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 19
 */

fun View.visible(){
    this.visibility=View.VISIBLE
}

fun View.gone(){
    this.visibility=View.GONE
}