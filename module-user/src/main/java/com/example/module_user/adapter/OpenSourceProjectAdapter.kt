package com.example.module_user.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.preCreateSession
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.webview.vassonic.SonicJavaScriptInterface
import com.example.module_user.OpenSourceProjectActivity
import com.example.module_user.R

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/2 13
 */
class OpenSourceProjectAdapter(private val activity:OpenSourceProjectActivity,private val dataList:List<OpenSourceBean>):RecyclerView.Adapter<OpenSourceProjectAdapter.ViewHolder>() {


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var name=view.findViewById<TextView>(R.id.name)
        var url=view.findViewById<TextView>(R.id.url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_source_project,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text=dataList[position].name
        holder.url.text=dataList[position].url
        holder.itemView.setOnClickListener {
            dataList[position].url.preCreateSession(activity)
            ARouter.getInstance().build(RouterActivityPath.WebView.PAGER_WEBVIEW)
                .withString(Const.WebViewActivity.TITLE, GlobalUtils.appName)
                .withString(Const.WebViewActivity.LINK_URL, dataList[position].url)
                .withBoolean(Const.WebViewActivity.IS_SHARE,true)
                .withBoolean(Const.WebViewActivity.IS_TITLE_FIXED,true)
                .withInt(Const.WebViewActivity.PARAM_MODE,Const.WebViewActivity.MODE_SONIC)
                .withLong(SonicJavaScriptInterface.PARAM_CLICK_TIME,System.currentTimeMillis())
                .navigation()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}