package com.example.librery_base.global

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.library_base.base.BaseApplication
import com.example.librery_base.webview.vassonic.SonicRuntimeImpl
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSessionConfig


/**
 *description : <p>
 *全局顶级方法和变量
 *</p>
 *
 *@author : flyli
 *@since :2021/5/23 19
 */
/**
 * datastore实例
 */
val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "_preferences",
produceMigrations = {context ->
    listOf(SharedPreferencesMigration(context,"_preferences"))
})

/**
 * 暴露给外部的datastore
 */
val dataStore:DataStore<Preferences> = BaseApplication.getInstance().dataStore


/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block() }
    v.forEach { it?.setOnClickListener(listener) }
}
/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param listener 处理点击事件监听器
 */
fun setOnClickListener(vararg v: View?, listener: View.OnClickListener) {
    v.forEach { it?.setOnClickListener(listener) }
}
/**
 * VasSession预加载session
 */
fun CharSequence.preCreateSession(context: Context): Boolean {
    if (!SonicEngine.isGetInstanceAllowed()) {
        SonicEngine.createInstance(SonicRuntimeImpl(context), SonicConfig.Builder().build())
    }
    val sessionConfigBuilder = SonicSessionConfig.Builder().apply { setSupportLocalServer(true) }
    val preloadSuccess = SonicEngine.getInstance().preCreateSession(this.toString(), sessionConfigBuilder.build())
    Log.d("preCreateSession()", "${this}\t:${if (preloadSuccess) "Preload start up success!" else "Preload start up fail!"}")
    return preloadSuccess
}


/**
 * 设置TextView图标
 *
 * @param drawable     图标
 * @param iconWidth    图标宽dp：默认自动根据图标大小
 * @param iconHeight   图标高dp：默认自动根据图标大小
 * @param direction    图标方向，0左 1上 2右 3下 默认图标位于左侧0
 */
fun TextView.setDrawable(drawable: Drawable?, iconWidth: Float? = null, iconHeight: Float? = null, direction: Int = 0) {
    if (iconWidth != null && iconHeight != null) {
        //第一个0是距左边距离，第二个0是距上边距离，iconWidth、iconHeight 分别是长宽
        drawable?.setBounds(0, 0, dp2px(iconWidth), dp2px(iconHeight))
    }
    when (direction) {
        0 -> setCompoundDrawables(drawable, null, null, null)
        1 -> setCompoundDrawables(null, drawable, null, null)
        2 -> setCompoundDrawables(null, null, drawable, null)
        3 -> setCompoundDrawables(null, null, null, drawable)
        else -> throw NoSuchMethodError()
    }
}

/**
 * 设置TextView图标
 *
 * @param lDrawable     左边图标
 * @param rDrawable     右边图标
 * @param lIconWidth    图标宽dp：默认自动根据图标大小
 * @param lIconHeight   图标高dp：默认自动根据图标大小
 * @param rIconWidth    图标宽dp：默认自动根据图标大小
 * @param rIconHeight   图标高dp：默认自动根据图标大小
 */
fun TextView.setDrawables(lDrawable: Drawable?, rDrawable: Drawable?, lIconWidth: Float? = null, lIconHeight: Float? = null, rIconWidth: Float? = null, rIconHeight: Float? = null) {
    if (lIconWidth != null && lIconHeight != null) {
        lDrawable?.setBounds(0, 0, dp2px(lIconWidth), dp2px(lIconHeight))
    }
    if (rIconWidth != null && rIconHeight != null) {
        rDrawable?.setBounds(0, 0, dp2px(rIconWidth), dp2px(rIconHeight))
    }
    setCompoundDrawables(lDrawable, null, rDrawable, null)
}

/**
 * 根据手机的分辨率将dp转成为px。
 */
fun dp2px(dp: Float): Int {
    val scale = BaseApplication.getInstance().resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp。
 */
fun px2dp(px: Float): Int {
    val scale = BaseApplication.getInstance().resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * 获取屏幕宽值。
 */
val screenWidth
    get() = BaseApplication.getInstance().resources.displayMetrics.widthPixels

/**
 * 获取屏幕高值。
 */
val screenHeight
    get() = BaseApplication.getInstance().resources.displayMetrics.heightPixels

/**
 * 获取屏幕像素：对获取的宽高进行拼接。例：1080X2340。
 */
fun screenPixel(): String {
    BaseApplication.getInstance().resources.displayMetrics.run {
        return "${widthPixels}X${heightPixels}"
    }
}

