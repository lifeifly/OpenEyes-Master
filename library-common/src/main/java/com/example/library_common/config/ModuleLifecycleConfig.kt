/*
 * @author flyli
 */

package com.example.library_common.config

import com.example.library_base.base.BaseApplication
import com.example.library_common.IModuleInit

/**
 *description : <p>
 *作为组件生命周期初始化的配置类，通过反射机制，动态调用每个组件初始化逻辑
 *</p>
 *
 *@author : flyli
 *@since :2021/5/18 23
 */
object ModuleLifecycleConfig {

    /**
     * 优先初始化
     */
    fun initModuleAhead(application: BaseApplication) {
        for (modelName in ModuleLifecycleRefixs.initModuleNames) {
            //获取全类名，运用反射
            val clazz = Class.forName(modelName)
            val init = clazz.newInstance () as IModuleInit
            //自身优先初始化组件
            init.onInitAhead(application)
        }
    }

    /**
     * 后初始化组件
     * @param application BaseApplication
     */
    fun initModuleBehind(application: BaseApplication){
        for (moduleName in ModuleLifecycleRefixs.initModuleNames){
            //获取全类名进行反射
            val clazz=Class.forName(moduleName)
            val init=clazz.newInstance() as IModuleInit
            //后初始化各个组件
            init.onInitBehind(application)
        }
    }
}