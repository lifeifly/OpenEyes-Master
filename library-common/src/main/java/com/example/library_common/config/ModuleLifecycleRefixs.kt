/*
 * @author flyli
 */

package com.example.library_common.config

/**
 *description : <p>
 *组件生命周期初始化类的类目管理者，在这里注册需要初始化的组件，通过反射动态调用各个组件的初始化方法
 *</p>
 *
 *@author : flyli
 *@since :2021/5/18 23
 */
object ModuleLifecycleRefixs {
    //基础库
    private const val BASE_INIT="com.example.library_common.CommonModuleInit"
    //main组件库
    private const val MAIN_INIT="com.example.module_main.moduleinit.MainModuleInit"

    //用户组件初始化
    private const val USER_INIT="com.example.module_user.UserModuleInit"
    //所有需要初始化的组件全类名,利用反射
    val initModuleNames= arrayOf(BASE_INIT,MAIN_INIT, USER_INIT)
}