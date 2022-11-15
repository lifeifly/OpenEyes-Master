package com.example.module_community.Recommend

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.module_community.R

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 21
 */
class HorizontalScrollItemCollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
}

class ItemHorizontalScrollCollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivBg = view.findViewById<ImageView>(R.id.ivBgPicture)
    val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
    val tvSubTitle = view.findViewById<TextView>(R.id.tvSubTitle)
}