package com.example.librery_base.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.example.library_base.base.BaseApplication

/**
 *description : <p>
 *吐司工具类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/20 13
 */
class ToastUtils {
    companion object {
        private var mToast: Toast? = null

        fun show( message: String) {
            if (!TextUtils.isEmpty(message)) {
                if (mToast != null) {
                    mToast?.cancel()
                }
                mToast = Toast.makeText(BaseApplication.getInstance(), "", Toast.LENGTH_LONG)
                mToast?.setText(message)
                mToast?.show()
            }
        }

        fun show(@StringRes res: Int) {
            val msg=BaseApplication.getInstance().resources.getString(res)
            if (!TextUtils.isEmpty(msg) ) {
                if (mToast != null) {
                    mToast?.cancel()
                }
                mToast = Toast.makeText(BaseApplication.getInstance(), "", Toast.LENGTH_LONG)
                mToast?.setText(msg)
                mToast?.show()
            }
        }
    }
}