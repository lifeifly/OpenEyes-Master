package com.example.module_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.umeng.message.PushAgent

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        PushAgent.getInstance(this).onAppStart()
        ImmersionBar.with(this).autoDarkModeEnable(true,0.2F)
            .statusBarColor(R.color.black).init()
    }
}