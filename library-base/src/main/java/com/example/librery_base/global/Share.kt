package com.example.librery_base.global

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.example.librery_base.share.ShareDialogFragment
import com.example.librery_base.utils.ShareUtils

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/24 21
 */
/**
 * 调用系统原生分享。
 *
 * @param activity 上下文
 * @param shareContent 分享内容
 * @param shareType SHARE_MORE=0，SHARE_QQ=1，SHARE_WECHAT=2，SHARE_WEIBO=3，SHARE_QQZONE=4
 */
fun share(activity: Activity, shareContent: String, shareType: Int) {
    ShareUtils.share(activity, shareContent, shareType)
}

/**
 * 弹出分享对话框。
 *
 * @param activity 上下文
 * @param shareContent 分享内容
 */
fun showDialogShare(activity: Activity, shareContent: String) {
    if (activity is AppCompatActivity) {
        ShareDialogFragment()
            .showDialog(activity, shareContent)
    }
}