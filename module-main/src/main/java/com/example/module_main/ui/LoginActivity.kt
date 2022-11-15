package com.example.module_main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Trace
import android.view.View
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingX2C
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.library_common.Const
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.librery_base.global.setDrawable
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_main.R
import com.example.module_main.databinding.ActivityLoginBinding
import com.umeng.message.PushAgent
import com.zhangyue.we.x2c.X2C
import com.zhangyue.we.x2c.ano.Xml

@Route(path = RouterActivityPath.Login.PAGER_LOGIN)
@Xml(layouts = ["activity_login"])
class LoginActivity : AppCompatActivity() {
    var _binding: ActivityLoginBinding? = null

    val binding: ActivityLoginBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        X2C.setContentView(this,R.layout.activity_login)
        _binding= ActivityLoginBinding.bind(findViewById(R.id.login_container))
        PushAgent.getInstance(this).onAppStart()

        initView()
        initListener()
    }

    private fun initListener() {
        setOnClickListener(
            binding.titleBar.tvRight,
            binding.titleBar.ivBack,
            binding.loginTvUserLogin,
            binding.loginTvRegister,
            binding.loginTvAuthor,
            binding.loginTvProtocol,
            binding.loginIvWechat,
            binding.loginIvWeibo,
            binding.loginIvQq
        ) {
            when (this) {
                binding.titleBar.tvRight -> {
                    WebViewActivity.start(
                        this@LoginActivity, WebViewActivity.DEFAULT_TITLE,
                        Const.Url.FORGET_PASSWORD, false, false
                    )
                }

                binding.loginTvRegister -> {
                    WebViewActivity.start(
                        this@LoginActivity, WebViewActivity.DEFAULT_TITLE,
                        Const.Url.AUTHOR_REGISTER, false, false
                    )
                }
                binding.loginTvAuthor -> {
                    WebViewActivity.start(
                        this@LoginActivity, WebViewActivity.DEFAULT_TITLE,
                        Const.Url.AUTHOR_LOGIN, false, false
                    )
                }
                binding.loginTvProtocol -> {
                    WebViewActivity.start(
                        this@LoginActivity, WebViewActivity.DEFAULT_TITLE,
                        Const.Url.USER_AGREEMENT, false, false
                    )
                }
                binding.loginTvUserLogin, binding.loginIvWechat, binding.loginIvWeibo, binding.loginIvQq -> {
                    ToastUtils.show(R.string.currently_not_supported)
                }
                binding.titleBar.ivBack -> {
                    finish()
                }
            }
        }
    }

    private fun initView() {
        binding.titleBar.titleBarContainer.layoutParams.height =
            resources.getDimensionPixelSize(R.dimen.actionBarSize)
        binding.titleBar.titleBarContainer.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
        val padding = dp2px(9F)
        binding.titleBar.ivBack.setPadding(padding, padding, padding, padding)
        binding.titleBar.ivBack.setImageResource(R.drawable.ic_close_white_24dp)
        binding.titleBar.tvRight.visibility = View.VISIBLE
        binding.titleBar.tvRight.text = GlobalUtils.getString(R.string.forgot_password)
        binding.titleBar.tvRight.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.titleBar.tvRight.textSize = 12F
        binding.titleBar.divider.visibility = View.GONE
        binding.loginTvAccount.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_person_white_18dp
            )
        )
        binding.loginTvPassword.setDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_password_white_lock_18dp
            )
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}