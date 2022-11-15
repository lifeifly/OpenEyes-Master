package com.example.library_common.tablayout

import com.flyco.tablayout.listener.CustomTabEntity

/**
 *description : <p>
 *与CommonTabLayout搭配使用的实体类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/24 23
 */
class TabEntity(private var title:String,private var selectedIcon:Int=0,private var unSelectedIcon:Int=0): CustomTabEntity {
    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }

    override fun getTabSelectedIcon(): Int {
       return selectedIcon
    }

    override fun getTabTitle(): String {
       return title
    }
}