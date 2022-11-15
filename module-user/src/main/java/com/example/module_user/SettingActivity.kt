package com.example.module_user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.librery_base.global.GlobalUtils
import com.example.module_user.databinding.ActivitySettingBinding
import com.gyf.immersionbar.ImmersionBar
import com.umeng.message.PushAgent

class SettingActivity : AppCompatActivity() {
    var _binding: ActivitySettingBinding? = null
    val binding: ActivitySettingBinding
        get() = _binding!!

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SettingViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PushAgent.getInstance(this).onAppStart()
        ImmersionBar.with(this)
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(R.color.colorPrimaryDark)
            .fitsSystemWindows(true).init()

        binding.viewModel = vm
        binding.lifecycleOwner = this
        initTitleBar()
    }

    private fun initTitleBar() {
        binding.titleBar.tvTitle.text = GlobalUtils.getString(R.string.settings)
        binding.titleBar.tvRight.setTextColor(
            ContextCompat.getColor(
                this@SettingActivity,
                R.color.white
            )
        )
        binding.titleBar.tvRight.textSize = 12f
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingActivity::class.java))
        }
    }
}