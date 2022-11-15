package com.example.librery_base.utils

import android.app.Activity
import android.content.Intent
import com.example.librery_base.R
import com.example.librery_base.global.GlobalUtils
import java.lang.Exception

/**
 *description : <p>
 *调用系统原生的分享工具类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/24 14
 */
const val SHARE_MORE = 0
const val SHARE_QQ = 1
const val SHARE_WECHAT = 2
const val SHARE_WEIBO = 3
const val SHARE_QQZONE = 4
const val SHARE_WECHAT_MEMORIES = 5

object ShareUtils {
    private fun processShare(activity: Activity, shareContent: String, shareType: Int) {
        when (shareType) {
            SHARE_QQ -> {
                if (!GlobalUtils.isQQInstalled()) {
                    ToastUtils.show(
                        GlobalUtils.getString(R.string.your_phone_does_not_install_qq)
                    )
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.tencent.mobileqq",
                    "com.tencent.mobileqq.activity.JumpActivity"
                )
            }
            SHARE_WECHAT -> {
                if (!GlobalUtils.isWechatInstalled()) {
                    ToastUtils.show(
                        GlobalUtils.getString(R.string.your_phone_does_not_install_wechat)
                    )
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.tencent.mm",
                    "com.tencent.mm.ui.tools.ShareImgUI"
                )
            }
            SHARE_WEIBO -> {
                if (!GlobalUtils.isWeiboInstalled()) {
                    ToastUtils.show(

                        GlobalUtils.getString(R.string.your_phone_does_not_install_weibo)
                    )
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.sina.weibo",
                    "com.sina.weibo.composerinde.ComposerDispatchActivity"
                )
            }
            SHARE_QQZONE -> {
                if (!GlobalUtils.isQQInstalled()) {
                    ToastUtils.show(

                        GlobalUtils.getString(R.string.your_phone_does_not_install_qq)
                    )
                    return
                }
                share(
                    activity,
                    shareContent,
                    "com.qzone",
                    "com.qzonex.module.operation.ui.QZonePublishMoodActivity"
                )
            }
            SHARE_MORE -> {
                share(
                    activity,
                    shareContent
                )
            }
        }
    }

    private fun share(
        activity: Activity,
        shareContent: String,
        packageName: String,
        className: String
    ) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareContent)
                setClassName(packageName, className)

            }
            activity.startActivity(shareIntent)
        } catch (e: Exception) {
            ToastUtils.show( "分享出现异常")
        }
    }

    private fun share(activity: Activity, shareContent: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareContent)
        }
        activity.startActivity(Intent.createChooser(shareIntent, "分享到"))
    }

    /**
     * 调用系统原生分享
     */
    fun share(activity: Activity,shareContent: String,shareType: Int){
        processShare(
            activity,
            shareContent,
            shareType
        )
    }
}