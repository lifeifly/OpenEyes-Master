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
 *@since :2021/5/31 22
 */
class CommunityFollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPicture = view.findViewById<ImageView>(R.id.ivPicture)
    val tvChoicness = view.findViewById<TextView>(R.id.tvChoicness)
    val ivLayers = view.findViewById<ImageView>(R.id.ivLayers)
    val ivPlay = view.findViewById<ImageView>(R.id.play)
    val tvDes = view.findViewById<TextView>(R.id.tvDes)
    val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
    val ivRoundAvatar = view.findViewById<ImageView>(R.id.ivRoundAvatar)
    val tvName = view.findViewById<TextView>(R.id.tvName)
    val tvCollect = view.findViewById<TextView>(R.id.tvCollect)
}