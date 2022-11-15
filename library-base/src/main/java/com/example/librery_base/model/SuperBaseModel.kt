package com.example.librery_base.model

import android.os.Handler
import android.os.Looper
import com.example.librery_base.utils.GsonUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentLinkedDeque


/**
 *description : <p>
 *基类抽象model
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 23
 */
abstract class SuperBaseModel<T> {
    //主线程handler
    protected val mHandler= Handler(Looper.getMainLooper())
    protected var compositeDisposable:CompositeDisposable?=null
    //虚引用和引用队列结合使用,虚引用中的对象被回收就会加入引用队列
    //引用队列
    protected val mReferenceQueue:ReferenceQueue<IBaseModelListener>

    //线程安全队列
    protected val mWeakReferenceDueue:ConcurrentLinkedDeque<WeakReference<IBaseModelListener>>


    init {
        mReferenceQueue= ReferenceQueue()
        mWeakReferenceDueue= ConcurrentLinkedDeque()
    }

    /**
     * 对具体业务model进行注册区分
     * @param listener IBaseModelListener
     */
    @Synchronized
    fun register(listener: IBaseModelListener){
        //每次注册的时候清理已经被系统回收的对象
        var releasedListener: Reference<out IBaseModelListener>? = mReferenceQueue.poll()
        while (releasedListener!=null){
            //删除被回收的对象
            mWeakReferenceDueue.remove(releasedListener)
            releasedListener=mReferenceQueue.poll()
        }
        //如果队列中还存在此对象，就不用注册了
        for (weakListener in mWeakReferenceDueue){
            val listenerItem=weakListener.get()
            if (listenerItem==listener){
                return
            }
        }
        //队列中不存在此对象，注册该对象
        val weakListener=WeakReference(listener,mReferenceQueue)
        mWeakReferenceDueue.add(weakListener)
    }

    /**
     * 取消注册
     * @param listener IBaseModelListener指定被取消注册的对象
     */
    @Synchronized
    fun unregister(listener: IBaseModelListener){
        for (weakListener in mWeakReferenceDueue){
            val listenerItem=weakListener.get()
            if (listenerItem==listener){
                //存在次对象就取消注册
                mWeakReferenceDueue.remove(weakListener)
                break
            }
        }
    }

    /**
     * 获取缓存的数据类型
     * @return Type
     */
    protected fun getTClass():Type?{
        return null
    }

    /**
     * 该model的数据是否有apk预制的数据，如果有请返回
     * @return String?
     */
    protected fun getApkCache():String?{
        return null
    }

    /**
     * 是否需要更新数据，默认每一次都需要更新
     * @return Boolean
     */
    protected fun isNeedToUpdate():Boolean{
        return true
    }

    /**
     * 获取缓存数据并加载网络数据
     */
    protected fun getCacheDataAndLoad(){
        //如果有apk内置数据，加载内置数据
        if (getApkCache()!=null){
            notifyCacheData(GsonUtils.fromLocalGson<T>(getApkCache()!!,getTClass()!!))
        }
    }

    /**
     * 加载缓存数据
     * @param json T
     */
    protected abstract fun notifyCacheData(json: T)

    /**
     * 加载网络数据
     */
    protected abstract fun load()

    /**
     * 订阅对象管理
     * @param disposable Disposable
     */
    fun addDisposable(disposable: Disposable){
        if (compositeDisposable==null){
            compositeDisposable= CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    /**
     * 取消所有订阅
     */
    fun cancel(){
        if (compositeDisposable!=null&&!compositeDisposable?.isDisposed!!){
            //有对象
            compositeDisposable?.isDisposed
        }
    }
}