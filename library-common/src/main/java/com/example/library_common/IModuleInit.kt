/*
 * @author flyli
 */

package com.example.library_common

import com.example.library_base.base.BaseApplication

/**
 *description : <p>
 *动态配置组件application，有需要初始化的组件实现该接口，同意在宿主app的application中进行初始化
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 12
 */
interface IModuleInit {
    //需要优先初始化的
    fun onInitAhead(application: BaseApplication):Boolean

    //可以后初始化的
    fun onInitBehind(application: BaseApplication):Boolean
}