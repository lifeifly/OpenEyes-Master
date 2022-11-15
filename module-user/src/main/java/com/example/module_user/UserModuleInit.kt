package com.example.module_user

import com.example.library_base.base.BaseApplication
import com.example.library_common.IModuleInit

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 22
 */
class UserModuleInit:IModuleInit {


    override fun onInitAhead(application: BaseApplication): Boolean {
        return false
    }

    override fun onInitBehind(application: BaseApplication): Boolean {
        return false
    }


}