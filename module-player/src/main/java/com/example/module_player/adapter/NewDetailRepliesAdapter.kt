package com.example.module_player.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.library_common.Const
import com.example.library_common.recyclerview.HeaderFourViewHolder
import com.example.library_common.recyclerview.RecyclerViewHelper
import com.example.library_network.bean.VideoReplies
import com.example.librery_base.BuildConfig
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.recyclerview.BaseRvAdapter
import com.example.librery_base.utils.ToastUtils
import com.example.module_player.NewDetailActivity
import com.example.module_player.R
import com.eyepetizer.android.util.DateUtil

/**
 *description : <p>
 *视频详情-评论列表的适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/28 16
 */
class NewDetailRepliesAdapter(
    private val activity: NewDetailActivity,
    val dataList: List<VideoReplies.Item>
) : BaseRvAdapter<VideoReplies.Item, RecyclerView.ViewHolder>(diff) {

    override fun getItemCount(): Int = dataList.size

    override fun getItemVT(position: Int): Int = when {
        dataList[position].type == "reply" && dataList[position].data.dataType == "ReplyBeanForClient"
        -> REPLY_BEAN_FOR_CLIENT_TYPE
        else -> RecyclerViewHelper.getItemViewType(dataList[position])
    }

    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is HeaderFourViewHolder -> {
                holder.apply {
                    tvHeaderTitle4.text = item.data.text
                    if (item.data.actionUrl != null && item.data.actionUrl?.startsWith(Const.ActionUrl.REPLIES_HOT)!!) {
                        //热门评论
                        ivHeaderRight4.visible()
                        holder.tvHeaderTitle4.layoutParams =
                            (holder.tvHeaderTitle4.layoutParams as ConstraintLayout.LayoutParams).apply {
                                setMargins(0, dp2px(30F), 0, dp2px(6F))
                            }
                        setOnClickListener(holder.tvHeaderTitle4, holder.ivHeaderRight4) {
                            ToastUtils.show(R.string.currently_not_supported)
                        }
                    } else {
                        ivHeaderRight4.gone()
                        //相关推荐、最新评论
                        holder.tvHeaderTitle4.layoutParams =
                            (holder.tvHeaderTitle4.layoutParams as ConstraintLayout.LayoutParams).apply {
                                setMargins(0, dp2px(24F), 0, dp2px(6F))
                            }
                    }
                }
            }

            is ReplyViewHolder -> {
                holder.apply {
                    replyGroup.gone()
                    ivAvatar.load(item.data.user?.avatar ?: "")
                    tvName.text = item.data.user?.nickname
                    tvLike.text =
                        if (item.data.likeCount == 0) "" else item.data.likeCount.toString()
                    tvComment.text = item.data.message
                    tvTime.text = getTimeReply(item.data.createTime)
                    ivLike.setOnClickListener {
                        ToastUtils.show(R.string.currently_not_supported)
                    }
                    item.data.parentReply?.run {
                        replyGroup.visible()
                        tvReplyUser.text = String.format(
                            GlobalUtils.getString(R.string.reply_target),
                            user?.nickname
                        )
                        ivReplyAvatar.load(user?.avatar ?: "")
                        tvRelyName.text = user?.nickname
                        tvReplyComment.text = message
                        tvShowConversation.setOnClickListener {
                            ToastUtils.show(R.string.currently_not_supported)
                        }
                    }
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) ToastUtils.show("${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}")
            }
        }
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            REPLY_BEAN_FOR_CLIENT_TYPE -> ReplyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_new_detail_replies, parent, false)
            )
            else -> RecyclerViewHelper.getViewHolder(parent, viewType)
        }

    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvComment = view.findViewById<TextView>(R.id.tv_comment)
        val tvLike = view.findViewById<TextView>(R.id.tv_like)
        val ivLike = view.findViewById<ImageView>(R.id.iv_like)
        val tvReplyUser = view.findViewById<TextView>(R.id.tv_reply_user)

        val replyGroup = view.findViewById<Group>(R.id.reply_group)
        val ivReplyAvatar = view.findViewById<ImageView>(R.id.iv_reply_avatar)
        val tvRelyName = view.findViewById<TextView>(R.id.tv_reply_name)
        val tvReplyComment = view.findViewById<TextView>(R.id.tv_reply_comment)

        val tvShowConversation = view.findViewById<TextView>(R.id.tv_rely_showConversation)
        val tvTime = view.findViewById<TextView>(R.id.tv_time)
        val tvReply = view.findViewById<TextView>(R.id.tv_reply)
        val ivMore = view.findViewById<ImageView>(R.id.iv_more)

    }

    private fun getTimeReply(dateMillis: Long): String {
        val currentMillis = System.currentTimeMillis()
        val timePast = currentMillis - dateMillis
        if (timePast > -DateUtil.MINUTE) { // 采用误差一分钟以内的算法，防止客户端和服务器时间不同步导致的显示问题
            when {
                timePast < DateUtil.DAY -> {
                    var pastHours = timePast / DateUtil.HOUR
                    if (pastHours <= 0) {
                        pastHours = 1
                    }
                    return DateUtil.getDate(dateMillis, "HH:mm")
                }
                else -> return DateUtil.getDate(dateMillis, "yyyy/MM/dd")
            }
        } else {
            return DateUtil.getDate(dateMillis, "yyyy/MM/dd HH:mm")
        }
    }

    companion object {

        const val TAG = "NewDetailRepliesAdapter"
        const val REPLY_BEAN_FOR_CLIENT_TYPE = Const.ItemViewType.MAX
        val diff = object : DiffUtil.ItemCallback<VideoReplies.Item>() {
            override fun areItemsTheSame(
                oldItem: VideoReplies.Item,
                newItem: VideoReplies.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: VideoReplies.Item,
                newItem: VideoReplies.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}