package com.example.module_community.attention

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.recyclerview.EmptyViewHolder
import com.example.library_common.utils.DateTimeUtils
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.library_network.bean.CommunityAttentionBean
import com.example.library_video.startAutoPlay
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_community.BuildConfig
import com.example.module_community.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

/**
 *description : <p>
 *社区-关注的适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/6/1 17
 */
class CAttentionAdapter(
    private val fragment: CAttentionFragment,
    private val dataList: List<CommunityAttentionBean.Item>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Const.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_community_header, parent, false)
            )
            Const.ItemViewType.MAX -> AutoPlayViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_community_auto_play, parent, false)
            )
            else -> EmptyViewHolder(View(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.itemView.setOnClickListener {
                ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
            }
            is AutoPlayViewHolder -> {
                val item = dataList[position - 1]
                item.data.content.data.run {
                    holder.ivAvatar.load(item.data.header.icon ?: author?.icon ?: "")

                    holder.tvReleaseTime.text = DateTimeUtils.format(
                        releaseTime ?: author?.latestReleaseTime ?: System.currentTimeMillis(),
                        "HH:mm"
                    )
                    holder.tvTitle.text = title
                    holder.tvName.text = author?.name ?: ""
                    holder.tvContent.text=description
                    holder.tvCollect.text=consumption.collectionCount.toString()
                    holder.tvReply.text=consumption.replyCount.toString()
                    holder.tvVideoDuration.text=DateTimeUtils.formatMillSecond(duration.toLong())
                    //配置VideoPlayer
                    startAutoPlay(fragment.requireActivity(),holder.videoPlayer,position,playUrl,cover.feed,TAG,object :GSYSampleCallBack(){
                        //准备中
                        override fun onPrepared(url: String?, vararg objects: Any?) {
                            super.onPrepared(url, *objects)
                            holder.tvVideoDuration.gone()
                            //静音
                            GSYVideoManager.instance().isNeedMute=true
                        }
                        //点击了暂停状态下的开始按键--->播放
                        override fun onClickResume(url: String?, vararg objects: Any?) {
                            super.onClickResume(url, *objects)
                            holder.tvVideoDuration.visible()
                        }
                        //点击了空白区域
                        override fun onClickBlank(url: String?, vararg objects: Any?) {
                            super.onClickBlank(url, *objects)
                            holder.tvVideoDuration.visible()
                            val videoInfo=NewDetailViewModel.VideoInfo(id.toLong(),playUrl,title,description,category,library,consumption,cover,author,webUrl)
                            //跳转到NewDetailActivity
                            ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                                .withParcelable(Const.ExtraTag.EXTRA_VIDEOINFO,videoInfo)
                                .navigation()
                        }
                    })
                    holder.let {
                        setOnClickListener(it.videoPlayer.thumbImageView,it.itemView,it.ivCollect,it.tvCollect,
                        it.ivFavorite,it.tvFavorite,it.ivShare){
                            when(this){
                                it.videoPlayer.thumbImageView,it.itemView->{
                                    val videoInfo=NewDetailViewModel.VideoInfo(id.toLong(),playUrl,title,description,category,library,consumption,cover,author,webUrl)
                                    //跳转到NewDetailActivity
                                    ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                                        .withParcelable(Const.ExtraTag.EXTRA_VIDEOINFO,videoInfo)
                                        .navigation()
                                }
                                it.ivCollect,it.tvCollect,it.ivFavorite,it.tvFavorite->{
                                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN)
                                        .navigation()
                                }
                                it.ivShare->{
                                    showDialogShare(fragment.requireActivity(),getShareContent(item))
                                }
                            }
                        }
                    }
                }
            }
            else ->{
                holder.itemView.gone()
                if (BuildConfig.DEBUG)ToastUtils.show("${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}")
            }
        }
    }

    private fun getShareContent(item: CommunityAttentionBean.Item): String {
        item.data.content.data.run {
            val linkUrl = "${item.data.content.data.webUrl.raw}&utm_campaign=routine&utm_medium=share&utm_source=others&uid=0&resourceType=${resourceType}"
            return "${title}|${GlobalUtils.appName}：\n${linkUrl}"
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> Const.ItemViewType.CUSTOM_HEADER
            dataList[position - 1].type == "autoPlayFollowCard" && dataList[position - 1].data.dataType == "FollowCard" -> Const.ItemViewType.MAX
            else -> Const.ItemViewType.UNKNOWN
        }
    }

    override fun getItemCount(): Int = dataList.size + 1

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class AutoPlayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val ivAvatarStar = view.findViewById<ImageView>(R.id.ivAvatarStar)
        val tvContent=view.findViewById<TextView>(R.id.tvContent)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvReleaseTime = view.findViewById<TextView>(R.id.tvReleaseTime)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val videoPlayer = view.findViewById<GSYVideoPlayer>(R.id.videoPlayer)
        val tvVideoDuration = view.findViewById<TextView>(R.id.tvVideoDuration)
        val ivCollect = view.findViewById<ImageView>(R.id.ivCollect)
        val tvCollect = view.findViewById<TextView>(R.id.tvCollect)
        val ivReply = view.findViewById<ImageView>(R.id.ivReply)
        val tvReply = view.findViewById<TextView>(R.id.tvReply)
        val ivFavorite = view.findViewById<ImageView>(R.id.ivFavorite)
        val tvFavorite = view.findViewById<TextView>(R.id.tvFavorite)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)


    }
    companion object{
        const val TAG="CAttentionAdapter"
    }
}