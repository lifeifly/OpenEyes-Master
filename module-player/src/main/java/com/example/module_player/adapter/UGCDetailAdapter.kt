package com.example.module_player.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.recyclerview.EmptyViewHolder
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.extension.*
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_player.BuildConfig
import com.example.module_player.R
import com.example.module_player.UGCDetailActivity
import com.github.chrisbanes.photoview.PhotoView
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 10
 */
class UGCDetailAdapter(
    private val activity: UGCDetailActivity,
    private val dataList: List<CommunityCommedBean.Item>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FOLLOW_CARD_TYPE -> FollowCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_ugc_detail, parent, false)
            )
            else -> EmptyViewHolder(View(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FollowCardViewHolder -> {
                val item = dataList[position]
                holder.run {
                    videoPlayer.gone()
                    viewpagerPhotos.gone()
                    controllHeader.visible()
                    clUserInfo.visible()

                    ivPullDown.setOnClickListener {
                        activity.finish()
                    }
                    item.data.content.let {
                        ivAvatar.load(it.data.owner.avatar)
                        if (it.data.owner.expert) ivAvatarStar.visible() else ivAvatarStar.gone()

                        tvName.text = it.data.owner.nickname

                        tvDes.text = it.data.owner.description
                        if (it.data.description.isBlank()) tvDes.gone() else tvDes.visible()


                        if (it.data.tags.isNullOrEmpty()) {
                            tvTagName.gone()
                        } else {
                            tvTagName.visible()
                            tvTagName.text = it.data.tags.run {
                                this?.first()?.name
                            }
                        }

                        tvCollect.text =
                            it.data.consumption.realCollectionCount.toString()

                        tvRely.text = it.data.consumption.replyCount.toString()
                    }

                    setOnClickListener(
                        tvPrivateLetter,
                        tvFollow,
                        ivCollect,
                        tvCollect,
                        ivReply,
                        tvRely,
                        ivFavorite,
                        tvFavorite,
                        ivShare
                    ) {
                        when (this) {
                            tvPrivateLetter, tvFollow, ivCollect, tvCollect, ivFavorite, tvFavorite -> {
                                ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN)
                                    .navigation()
                            }
                            ivShare -> {
                                showDialogShare(activity, getShareContent(item))
                            }
                            ivReply, tvRely -> ToastUtils.show(GlobalUtils.getString(R.string.currently_not_supported))
                            else -> {
                            }
                        }
                    }

                    itemView.setOnClickListener {
                        switchHeaderAndUgcInfoVisibility()
                    }
                }
                when (item.data.content.type) {
                    STR_VIDEO_TYPE -> {
                        holder.videoPlayer.visible()
                        holder.videoPlayer.run {
                            val data = item.data.content.data
                            val cover = ImageView(activity)
                            cover.scaleType = ImageView.ScaleType.CENTER_CROP
                            cover.load(data.cover.detail)
                            cover.parent?.run { removeView(cover) }
                            thumbImageView = cover
                            setThumbPlay(true)
                            setIsTouchWiget(false)
                            isLooping = true
                            playTag = TAG
                            playPosition = position
                            setVideoAllCallBack(object : GSYSampleCallBack() {
                                override fun onClickBlank(url: String?, vararg objects: Any?) {
                                    super.onClickBlank(url, *objects)
                                    holder.switchHeaderAndUgcInfoVisibility()
                                }
                            })
                            setUp(data.playUrl, false, null)
                        }
                    }
                    STR_UGC_PICTURE_TYPE -> {
                        holder.viewpagerPhotos.visible()
                        item.data.content.data.urls?.run {
                            holder.viewpagerPhotos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                            holder.viewpagerPhotos.offscreenPageLimit = 1
                            holder.viewpagerPhotos.adapter = PhotosAdapter(this, holder)
                            if (item.data.content.data.urls?.size?:0 > 1) {
                                holder.tvCount.visible()
                                holder.tvCount.text = String.format(
                                    GlobalUtils.getString(R.string.photo_count),
                                    1,
                                    item.data.content.data.urls?.size
                                )

                            } else {
                                holder.tvCount.gone()
                            }
                            holder.viewpagerPhotos.registerOnPageChangeCallback(object :
                                ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    holder.tvCount.text = String.format(
                                        GlobalUtils.getString(R.string.photo_count),
                                        position + 1,
                                        item.data.content.data.urls?.size
                                    )
                                }
                            })
                        }
                    }
                    else -> {
                    }
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) ToastUtils.show("${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}")
            }
        }
    }

    private fun getShareContent(item: CommunityCommedBean.Item): String {
        item.data.content.data.run {
            val linkUrl =
                "https://www.eyepetizer.net/detail.html?vid=${id}&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0&resourceType=${resourceType}"
            return "${owner.nickname} 在${GlobalUtils.appName}发表了作品：\n「${description}」\n${linkUrl}"
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        return when (item.type) {
            STR_COMMUNITY_COLUMNS_CARD -> {
                if (item.data.dataType == STR_FOLLOW_CARD_DATA_TYPE) FOLLOW_CARD_TYPE
                else Const.ItemViewType.UNKNOWN
            }
            else -> Const.ItemViewType.UNKNOWN
        }
    }

    override fun getItemCount(): Int = dataList.size


    class FollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoPlayer = view.findViewById<GSYVideoPlayer>(R.id.detail_videoplayer)
        val viewpagerPhotos = view.findViewById<ViewPager2>(R.id.viewpager_photo)

        val controllHeader = view.findViewById<FrameLayout>(R.id.controll_header)
        val ivPullDown = view.findViewById<ImageView>(R.id.iv_pull_down)
        val tvCount = view.findViewById<TextView>(R.id.tv_photo_count)

        val clUserInfo = view.findViewById<ConstraintLayout>(R.id.cl_user_info)
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_avatar)
        val ivAvatarStar = view.findViewById<ImageView>(R.id.iv_avatar_star)
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvDes = view.findViewById<TextView>(R.id.tv_des)
        val tvFollow = view.findViewById<TextView>(R.id.tv_follow)
        val tvPrivateLetter = view.findViewById<TextView>(R.id.tv_private_letter)
        val tvTagName = view.findViewById<TextView>(R.id.tv_tag_name)

        val ivCollect = view.findViewById<ImageView>(R.id.iv_colloect)
        val ivReply = view.findViewById<ImageView>(R.id.iv_reply)
        val ivFavorite = view.findViewById<ImageView>(R.id.iv_favorities)
        val ivShare = view.findViewById<ImageView>(R.id.iv_share)
        val tvCollect = view.findViewById<TextView>(R.id.tv_collect)
        val tvRely = view.findViewById<TextView>(R.id.tv_reply)
        val tvFavorite = view.findViewById<TextView>(R.id.tv_favorities)


        fun switchHeaderAndUgcInfoVisibility() {
            if (ivPullDown.visibility == View.VISIBLE) {
                ivPullDown.invisibleAlphaAnimation()
                clUserInfo.invisibleAlphaAnimation()
            } else {
                ivPullDown.visibleAlphaAnimation()
                clUserInfo.visibleAlphaAnimation()
            }
        }
    }

    inner class PhotosAdapter(
        private val dataList: List<String>,
        private val ugcHolder: FollowCardViewHolder
    ) : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {
        inner class ViewHolder(private val view: PhotoView) : RecyclerView.ViewHolder(view) {
            val photoView = view
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val photoView = PhotoView(parent.context)
            photoView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            return ViewHolder(photoView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.photoView.load(dataList[position])
            holder.photoView.setOnClickListener { ugcHolder.switchHeaderAndUgcInfoVisibility() }
        }

        override fun getItemCount(): Int = dataList.size
    }

    companion object {
        const val TAG = "UGCDetailAdapter"

        const val STR_VIDEO_TYPE = "video"
        const val STR_UGC_PICTURE_TYPE = "ugcPicture"
        const val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"
        const val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"
        const val FOLLOW_CARD_TYPE = 3
    }
}