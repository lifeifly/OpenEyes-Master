/*
 * @author flyli
 */

package com.example.library_base.base

import android.app.Activity
import java.util.*

/**
 *description : <p>
 *activity的堆栈管理者
 *</p>
 *
 *@author : flyli
 *@since :2021/5/18 22
 */
object AppManager {
    private var activityStack:Stack<Activity>?=null

    /**
     * 添加到堆栈
     * @param activity Activity 被添加的activity
     */
    fun addActivity(activity: Activity){
        if (activityStack==null){
            activityStack= Stack()
        }
        activityStack?.add(activity)
    }

    /**
     * 移除指定的activity
     * @param activity Activity 被移除的activity
     */
    fun removeActivity(activity: Activity){
        if (activityStack==null)return
        activityStack?.remove(activity)
    }

    /**
     * 是否还有activity
     * @return Boolean
     */
    fun isEmpty():Boolean{
        if (activityStack!=null&& !activityStack?.isEmpty()!!){
            return false
        }
        return true
    }

    /**
     * 获取当前的activity,最后一个压入的
     * @return Activity
     */
    fun getCurrentActivity():Activity?{
        if (activityStack!=null){
            return activityStack?.lastElement()
        }
        return null
    }

    /**
     * 结束指定的activity
     * @param activity Activity
     */
    fun finishActivity(activity: Activity){
        if (!activity.isFinishing){
            activity.finish()
        }
    }

    /**
     * 结束指定类型的activity
     * @param clazz Class<*>
     */
    fun finishActiviotyByClass(clazz: Class<*>){
        if (activityStack!=null&& activityStack?.size!!>0){
            for (i in activityStack!!){
                if (i.javaClass.equals(clazz)){
                    i.finish()
                    break
                }
            }
        }
    }

    /**
     * 结束所有的activity
     */
    fun finishAllActivity(){
        if (activityStack!=null&& activityStack?.size!!>0){
            for (i in 0..activityStack?.size!!){
                finishActivity(activityStack?.get(i)!!)
            }
        }
        //清空集合
        activityStack?.clear()
    }

    /**
     * 获取指定类型的activity
     * @param clazz Class<*>
     * @return Activity?
     */
    fun getActivityByClass(clazz: Class<*>):Activity?{
        if (activityStack!=null){
            for (i in activityStack!!){
                if (i.javaClass.equals(clazz)){
                    return i
                }
            }
        }
        return null
    }

    /**
     * 推出应用程序
     */
    fun exitApp(){
        finishAllActivity()
    }
}