package com.example.module_user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.module_user.adapter.OpenSourceBean
import com.example.module_user.adapter.OpenSourceProjectAdapter
import com.example.module_user.adapter.SimpleDividerItemDecoration
import com.example.module_user.databinding.ActivityOpenSourceProjectBinding
import com.gyf.immersionbar.ImmersionBar
import com.umeng.message.PushAgent

class OpenSourceProjectActivity : AppCompatActivity() {
    var _binding: ActivityOpenSourceProjectBinding? = null

    val binding: ActivityOpenSourceProjectBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityOpenSourceProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PushAgent.getInstance(this).onAppStart()

        ImmersionBar.with(this)
            .autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(R.color.colorPrimaryDark)
            .fitsSystemWindows(true).init()
        binding.titleBar.tvTitle.text=GlobalUtils.getString(R.string.open_source_project_list)
        val layoutManager=LinearLayoutManager(this)
        val adapter=OpenSourceProjectAdapter(this,getProjectList())
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager=layoutManager
        binding.recyclerView.addItemDecoration(SimpleDividerItemDecoration(this, dp2px(0.5F), ContextCompat.getColor(this, R.color.gray)))

        binding.titleBar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getProjectList(): List<OpenSourceBean> {
        return ArrayList<OpenSourceBean>().apply {
            add(OpenSourceBean("Retrofit", "https://github.com/square/retrofit"))
            add(OpenSourceBean("Glide", "https://github.com/bumptech/glide"))
            add(OpenSourceBean("OkHttp", "https://github.com/square/okhttp"))
            add(OpenSourceBean("Gson", "https://github.com/google/gson"))
            add(OpenSourceBean("Glide Transformations", "https://github.com/wasabeef/glide-transformations"))
            add(OpenSourceBean("Eventbus", "https://github.com/greenrobot/EventBus"))
            add(OpenSourceBean("Permissionx", "https://github.com/guolindev/PermissionX"))
            add(OpenSourceBean("FlycoTabLayout", "https://github.com/H07000223/FlycoTabLayout"))
            add(OpenSourceBean("SmartRefreshLayout", "https://github.com/scwang90/SmartRefreshLayout"))
            add(OpenSourceBean("BannerViewPager", "https://github.com/zhpanvip/BannerViewPager"))
            add(OpenSourceBean("Immersionbar", "https://github.com/gyf-dev/ImmersionBar"))
            add(OpenSourceBean("PhotoView", "https://github.com/chrisbanes/PhotoView"))
            add(OpenSourceBean("Circleimageview", "https://github.com/hdodenhof/CircleImageView"))
            add(OpenSourceBean("GSYVideoPlayer", "https://github.com/CarGuo/GSYVideoPlayer"))
            add(OpenSourceBean("VasSonic", "https://github.com/Tencent/VasSonic"))
            add(OpenSourceBean("Leakcanary", "https://github.com/square/leakcanary"))
            add(OpenSourceBean("Kotlinx Coroutines", "https://github.com/Kotlin/kotlinx.coroutines"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    companion object{
        fun start(context: Context){
            val intent=Intent(context,OpenSourceProjectActivity::class.java)
            context.startActivity(intent)
        }
    }
}