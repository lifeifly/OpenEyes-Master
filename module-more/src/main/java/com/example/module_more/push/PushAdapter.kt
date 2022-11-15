package com.example.module_more.push

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library_common.utils.ActionUrlUtils
import com.example.library_common.utils.DateTimeUtils
import com.example.library_network.bean.NotificationPushBean
import com.example.librery_base.extension.load
import com.example.module_more.R
import com.eyepetizer.android.util.DateUtil

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/2 17
 */
class PushAdapter(private val fragment: PushFragment,private val dataList:List<NotificationPushBean.Message>):RecyclerView.Adapter<PushAdapter.ViewHolder>() {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val ivAvatar=view.findViewById<ImageView>(R.id.ivAvatar)
        val tvTitle=view.findViewById<TextView>(R.id.tvTitle)
        val tvTime=view.findViewById<TextView>(R.id.tvTime)
        val tvContent=view.findViewById<TextView>(R.id.tvContent)
        val ivInto=view.findViewById<ImageView>(R.id.ivInto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_push_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item=dataList[position]
        holder.itemView.setOnClickListener {
            ActionUrlUtils.process(fragment,item.actionUrl,item.title)
        }
        holder.ivAvatar.load(item.icon){error(R.mipmap.ic_launcher)}
        holder.tvTitle.text=item.title
        holder.tvTime.text= DateUtil.getConvertedDate(item.date)
        holder.tvContent.text=item.content
    }

    override fun getItemCount(): Int =dataList.size
}