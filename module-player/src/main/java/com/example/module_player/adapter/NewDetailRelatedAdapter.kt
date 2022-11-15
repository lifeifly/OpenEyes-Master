package com.example.module_player.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.recyclerview.HeaderFourViewHolder
import com.example.library_common.recyclerview.RecyclerViewHelper
import com.example.library_common.recyclerview.VideoCardViewHolder
import com.example.library_common.utils.DateTimeUtils
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.library_network.bean.VideoRelated
import com.example.librery_base.BuildConfig
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.recyclerview.BaseRvAdapter
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_player.NewDetailActivity
import com.example.module_player.R

/**
 *description : <p>
 *视频相关推荐的适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/28 14
 */
class NewDetailRelatedAdapter(
    private val activity: NewDetailActivity,
    private val dataList: List<VideoRelated.Item>,
    private var videoInfoData: NewDetailViewModel.VideoInfo?
) : BaseRvAdapter<VideoRelated.Item, RecyclerView.ViewHolder>(diff) {
    companion object {
        const val TAG = "NewDetailRelatedAdapter"
        const val SIMPLE_HOT_REPLY_CARD_TYPE = Const.ItemViewType.MAX

        val diff = object : DiffUtil.ItemCallback<VideoRelated.Item>() {
            override fun areItemsTheSame(
                oldItem: VideoRelated.Item,
                newItem: VideoRelated.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: VideoRelated.Item,
                newItem: VideoRelated.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemVT(position: Int): Int = when {
        position == 0 -> Const.ItemViewType.CUSTOM_HEADER
        dataList[position - 1].type == "simpleHotReplyScrollCard"
                && dataList[position - 1].data.dataType == "ItemCollection" -> {
            SIMPLE_HOT_REPLY_CARD_TYPE
        }
        else -> RecyclerViewHelper.getItemViewType(dataList[position - 1])
    }

    override fun getItemCount(): Int {
        return dataList.size + 1
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Const.ItemViewType.CUSTOM_HEADER ->
                CustomVideoDetailHeaderViewHolder(
                    LayoutInflater.from(
                        parent.context
                    ).inflate(R.layout.item_video_info, parent, false)
                )
            SIMPLE_HOT_REPLY_CARD_TYPE
            -> SimpleHotRepliesViewHolder(View(parent.context))
            else -> RecyclerViewHelper.getViewHolder(parent, viewType)
        }
    }

    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomVideoDetailHeaderViewHolder -> {
                videoInfoData?.let {
                    holder.run {
                        itemNewDetailAuthorGroup.gone()
                        itemNewDetailTvTitle.text = it.title
                        itemNewDetailTvCategory.text = if (it.library == "DAILY")
                            "${it.category}/ 开眼精选"
                        else
                            "#${it.category}"
                        itemNewDetailTvDes.text = it.description
                        itemNewDetailTvCollection.text =
                            it.consumption.collectionCount.toString()
                        itemNewDetailTvShare.text = it.consumption.shareCount.toString()
                        it.author?.run {
                            itemNewDetailAuthorGroup.visible()
                            it.author?.icon?.let { itemNewDetailIvAvatar.load(it) }
                            itemNewDetailTvAuthorName.text = it.author?.name
                            itemNewDetailTvAuthorDes.text = it.author?.description
                        }
                        setOnClickListener(
                            itemNewDetailIvCollection,
                            itemNewDetailTvCollection,
                            itemNewDetailIvShare,
                            itemNewDetailTvShare,
                            itemNewDetailIvFavorite,
                            itemNewDetailTvFavorite,
                            itemNewDetailIvCache,
                            itemNewDetailTvCache,
                            itemNewDetailTvFollow
                        ) {
                            when (this) {
                                itemNewDetailIvCollection, itemNewDetailTvCollection, itemNewDetailIvFavorite, itemNewDetailTvFavorite -> ARouter.getInstance()
                                    .build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                                itemNewDetailIvShare, itemNewDetailTvShare -> showDialogShare(
                                    activity,
                                    "${it.title}:${it.webUrl.raw}"
                                )
                                itemNewDetailIvCache, itemNewDetailTvCache -> ToastUtils.show("该功能即将开放，敬请期待")
                                itemNewDetailTvFollow -> ARouter.getInstance()
                                    .build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                                else -> {
                                }
                            }
                        }
                    }
                }

            }
            is SimpleHotRepliesViewHolder -> {
                //不做任何处理
            }
            is HeaderFourViewHolder -> {
                val item = dataList[position - 1]
                holder.tvHeaderTitle4.text = item.data.text
            }
            is VideoCardViewHolder -> {
                val item = dataList[position - 1]
                holder.videoCardIvPicture.load(item.data.cover.feed, 4F)
                holder.videoCardTvDes.text =
                    if (item.data.library == "DAILY") "#${item.data.category} / 开眼精选"
                    else
                        "#${item.data.category}"

                holder.videoCardTvDes.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.videoCardTvTitle.text = item.data.title
                holder.videoCardTvTitle.setTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.white
                    )
                )
                holder.videoCardTvTime.text = DateTimeUtils.format(item.data.duration.toLong())

                holder.videoCardIvShare.setOnClickListener {
                    showDialogShare(activity, "${item.data.title}:${item.data.webUrl.raw}")
                }
                holder.itemView.setOnClickListener {
                    item.data.run {
                        NewDetailActivity.start(
                            activity,
                            NewDetailViewModel.VideoInfo(
                                id,
                                playUrl,
                                title,
                                description,
                                category,
                                library,
                                consumption,
                                cover,
                                author,
                                webUrl
                            )
                        )
                        activity.scrollTop()
                    }
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) ToastUtils.show("${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}")
            }
        }
    }

    fun bindVideoInfo(videoInfoData: NewDetailViewModel.VideoInfo) {
        this.videoInfoData = videoInfoData
    }


    /**
     * 视频信息简介的头部
     * @property itemNewDetailTvTitle (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvCategory (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvDes (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvFavorite (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvShare (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvCache (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvCollection (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvAuthorName (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvAuthorDes (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailTvFollow (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailIvCollection (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailIvShare (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailIvCache (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailIvFavorite (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailIvFold (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailIvAvatar (android.widget.TextView..android.widget.TextView?)
     * @property itemNewDetailAuthorGroup (android.widget.TextView..android.widget.TextView?)
     * @constructor
     */
    inner class CustomVideoDetailHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemNewDetailTvTitle = view.findViewById<TextView>(R.id.tv_title)
        val itemNewDetailTvCategory = view.findViewById<TextView>(R.id.tv_category)
        val itemNewDetailTvDes = view.findViewById<TextView>(R.id.tv_des)
        val itemNewDetailTvFavorite = view.findViewById<TextView>(R.id.tv_favorities)
        val itemNewDetailTvShare = view.findViewById<TextView>(R.id.tv_share)
        val itemNewDetailTvCache = view.findViewById<TextView>(R.id.tv_cache)
        val itemNewDetailTvCollection = view.findViewById<TextView>(R.id.tv_collection)
        val itemNewDetailTvAuthorName = view.findViewById<TextView>(R.id.tv_author_name)
        val itemNewDetailTvAuthorDes = view.findViewById<TextView>(R.id.tv_author_des)
        val itemNewDetailTvFollow = view.findViewById<TextView>(R.id.tv_follow)

        val itemNewDetailIvCollection = view.findViewById<ImageView>(R.id.iv_collection)
        val itemNewDetailIvShare = view.findViewById<ImageView>(R.id.iv_share)
        val itemNewDetailIvCache = view.findViewById<ImageView>(R.id.iv_cache)
        val itemNewDetailIvFavorite = view.findViewById<ImageView>(R.id.iv_favorities)
        val itemNewDetailIvFold = view.findViewById<ImageView>(R.id.iv_fold)
        val itemNewDetailIvAvatar = view.findViewById<ImageView>(R.id.iv_avatar)

        val itemNewDetailAuthorGroup = view.findViewById<Group>(R.id.item_video_info_group)


    }

    /**
     * 相关推荐的数据集合中附带热门评论，在此UI上不做处理，交给其他的adapter处理
     * @constructor
     */
    inner class SimpleHotRepliesViewHolder(view: View) : RecyclerView.ViewHolder(view)
}