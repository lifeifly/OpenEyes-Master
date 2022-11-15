/*
 * @author flyli
 */

package com.example.library_common.adapter

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.example.library_base.base.BaseApplication

/**
 *description : <p>
 *屏幕适配
 * 1.先在application中使用setUp方法初始化以下
 * 2.手动在activity中调用match方法适配，必须在setContentView之前
 * 3.建议使用dp做宽度适配
 *</p>
 *
 *@author : flyli
 *@since :2021/5/19 16
 */
class ScreenAutoAdapter {
    companion object {
        private const val MATCH_BASE_WIDTH = 0
        private const val MATCH_BASE_HEIGHT = 1

        /**
         * 适配单位
         */
        private const val MATCH_UNIT_DP = 0
        private const val MATCH_UNIT_PX = 1

        /**
         * 适配信息
         */
        private var mMatchInfo: MatchInfo? = null

        /**
         * activity生命周期监听
         */
        private var mActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks? = null

        /**
         * 初始化
         * @param application BaseApplication需要在application中初始化
         */
        fun setUp(application: BaseApplication) {
            /**
             * 获取屏幕分辨率的三种方法
             * 1.DisplayMetrics metrics = new DisplayMetrics()
             * Display display = activity.getWindowManager().getDefaultDisplay();
             * display.getMetrics(metrics);
             * 2.DisplayMetrics metrics=activity.getResources().getDisplayMetrics();
             * 3.Resources.getSystem().getDisplayMetrics();
             */
            //获取系统的displayMetrics
            val displayMetrics = application.resources.displayMetrics
            if (mMatchInfo == null) {//首次初始化时调用
                //记录系统的原始值
                mMatchInfo = MatchInfo(
                    displayMetrics.widthPixels, displayMetrics.heightPixels,
                    displayMetrics.density, displayMetrics.densityDpi,
                    displayMetrics.scaledDensity, displayMetrics.xdpi
                )
            }
            //添加字体变化的监听
            //调用Application#registerComponentCallbacks注册onConfigurationChanged监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {

                }

                override fun onConfigurationChanged(newConfig: Configuration) {
                    //字体改变后,将appScaleDensity重新赋值
                    if (newConfig.fontScale > 0) {
                        val scaledDensity = displayMetrics.scaledDensity
                        mMatchInfo?.appScaleDensity = scaledDensity
                    }
                }
            })
            /**
             * 在application中全局激活（也可单独使用match方法在指定的页面中配置适配）
             * @param application Application
             * @param designSize Float
             * @param matchBase Int
             * @param matchUnit Int
             */
            fun register(
                application: Application,
                designSize: Int,
                matchBase: Int,
                matchUnit: Int
            ) {
                if (mActivityLifecycleCallbacks == null) {
                    mActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
                        override fun onActivityPaused(activity: Activity) {
                            match(activity,designSize,matchBase,matchUnit)
                        }

                        override fun onActivityStarted(activity: Activity) {
                        }

                        override fun onActivityDestroyed(activity: Activity) {
                        }

                        override fun onActivitySaveInstanceState(
                            activity: Activity,
                            outState: Bundle
                        ) {
                        }

                        override fun onActivityStopped(activity: Activity) {
                        }

                        override fun onActivityCreated(
                            activity: Activity,
                            savedInstanceState: Bundle?
                        ) {
                        }

                        override fun onActivityResumed(activity: Activity) {
                        }
                    }
                    //注册activitycallback，所有activity都会执行
                    application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
                }
            }
        }

        /**
         * 全局取消适配
         * @param application Application
         * @param matchUnit IntArray
         */
        fun unregister(application: Application, vararg matchUnit: Int) {
            if (mActivityLifecycleCallbacks != null) {
                application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
                mActivityLifecycleCallbacks = null
            }
            for (i in matchUnit) {
                cancelMatch(application, i)
            }
        }

        /**
         * 屏幕适配，放在setContentView之前
         * @param context Context 上下文
         * @param designSize Int 设计图的尺寸
         */
        fun match(context: Context, designSize: Int) {
            match(context, designSize, MATCH_BASE_WIDTH, MATCH_UNIT_DP)
        }

        /**
         * 屏幕适配
         * @param context Context 上下文
         * @param designSize Int 设计图的尺寸
         * @param matchBaseWidth Int 适配基准
         */
        fun match(context: Context, designSize: Int, matchBaseWidth: Int) {
            match(context, designSize, matchBaseWidth, MATCH_UNIT_DP)
        }

        /**
         * 屏幕适配
         * @param context Context
         * @param designSize Int 设计图的尺寸
         * @param matchBaseWidth Int 适配基准
         * @param matchUnitDp Int 适配尺寸
         */
        private fun match(
            context: Context,
            designSize: Int,
            matchBaseWidth: Int,
            matchUnitDp: Int
        ) {
            if (designSize == 0) {
                throw UnsupportedOperationException("The designSize cannot be equals to 0")
            }
            if (matchUnitDp == MATCH_UNIT_DP) {
                matchByDp(context, designSize, matchBaseWidth)
            } else if (matchUnitDp == MATCH_UNIT_PX) {
                matchByPx(context, designSize, matchBaseWidth)
            }
        }


        /**
         *使用dp作为适配单位
         * dp和px之间的换算
         * <li>px = density * dp</li>
         * <li>density = dpi / 160</li>
         * <li>px = dp * (dpi / 160)</li>
         * </ul>
         * @param context Context
         * @param designSize Int
         * @param matchBase Int
         */
        private fun matchByDp(context: Context, designSize: Int, matchBase: Int) {
            val targetDensity: Float
            if (mMatchInfo != null) {
                if (matchBase == MATCH_BASE_WIDTH) {
                    //根据宽度适配
                    targetDensity = mMatchInfo?.screenWidth!! * 1F / designSize
                } else if (matchBase == MATCH_BASE_HEIGHT) {
                    //根据高度适配
                    targetDensity = mMatchInfo?.screenHeight!! * 1F /designSize
                }else{
                    //默认以宽度适配
                    targetDensity= mMatchInfo?.screenWidth!!*1F/designSize
                }
                val targetDensityDpi=targetDensity*160
                val targetScaledDensity=targetDensity* mMatchInfo?.appScaleDensity!!/ mMatchInfo?.appDensity!!
                val displayMetrics=context.resources.displayMetrics
                displayMetrics.density=targetDensity
                displayMetrics.densityDpi= targetDensityDpi.toInt()
                displayMetrics.scaledDensity=targetScaledDensity
            }

        }

        /**
         * 使用pt作为适配单位
         * pt转px pt*metrics.xdpi*1F/72
         * @param context Context
         * @param designSize Int
         * @param matchBase Int
         */
        private fun matchByPx(context: Context, designSize: Int, matchBase: Int) {
            val targetXdpi:Float
            if (mMatchInfo!=null){
                if (matchBase== MATCH_BASE_WIDTH){
                    targetXdpi= mMatchInfo?.screenWidth!!*72F/designSize
                }else if (matchBase== MATCH_BASE_HEIGHT){
                    targetXdpi= mMatchInfo?.screenHeight!!*72F/designSize
                }else{
                    targetXdpi= mMatchInfo?.screenWidth!!*72F/designSize
                }
                val displayMetrics = context.resources.displayMetrics
                displayMetrics.xdpi = targetXdpi
            }
        }

        /**
         * 重置适配信息，取消适配
         * @param context Context
         */
        private fun cancelMatch(context: Context) {
            cancelMatch(context, MATCH_UNIT_DP)
            cancelMatch(context, MATCH_UNIT_PX)
        }

        /**
         * 重置屏幕适配
         * @param application Application
         * @param i Int 需要取消适配的单位
         */
        private fun cancelMatch(context: Context, i: Int) {
            if (mMatchInfo != null) {
                val displayMetrics = context.resources.displayMetrics
                if (i == MATCH_UNIT_DP) {
                    if (displayMetrics.density != mMatchInfo?.appDensity) {
                        displayMetrics.density = mMatchInfo?.appDensity!!
                    }
                    if (displayMetrics.densityDpi != mMatchInfo?.appDensityDpi) {
                        displayMetrics.densityDpi = mMatchInfo?.appDensityDpi!!
                    }
                    if (displayMetrics.scaledDensity != mMatchInfo?.appScaleDensity) {
                        displayMetrics.scaledDensity = mMatchInfo?.appScaleDensity!!
                    }
                } else if (i == MATCH_UNIT_PX) {
                    if (displayMetrics.xdpi != mMatchInfo?.appXdpi) {
                        displayMetrics.xdpi = mMatchInfo?.appXdpi!!
                    }
                }
            }
        }

        fun getMatchInfo(): MatchInfo? {
            return mMatchInfo
        }
    }

    /**
     * 适配信息
     * @property screenWidth Int
     * @property screenHeight Int
     * @property appDensity Float
     * @property appDensityDpi Float
     * @property appScaleDensity Float
     * @property appXdpi Float
     * @constructor
     */
    data class MatchInfo(
        val screenWidth: Int,
        val screenHeight: Int,
        val appDensity: Float,
        val appDensityDpi: Int,
        var appScaleDensity: Float,
        val appXdpi: Float
    )
}