package com.example.module_home.daily

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.recyclerview.*
import com.example.library_common.utils.ActionUrlUtils
import com.example.library_common.utils.DateTimeUtils
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.library_network.bean.HomeDailyBean
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.invisible
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.recyclerview.BaseRvAdapter
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_home.BuildConfig
import com.example.module_home.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *description : <p>
 *首页-日报的适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/27 19
 */
class DailyAdapter(
    private val fragment: DailyFragment,
    private val dataList: List<HomeDailyBean.Item>
) : BaseRvAdapter<HomeDailyBean.Item, RecyclerView.ViewHolder>(diff) {
    override fun getItemCount(): Int = dataList.size

    override fun getItemVT(position: Int): Int =
        RecyclerViewHelper.getItemViewType(dataList[position])

    override fun createVH(parent: ViewGroup, viewType: Int) =
        RecyclerViewHelper.getViewHolder(parent, viewType)


    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is HeaderFiveViewHolder -> {
                holder.tvHeaderTitle5.text = item.data.text
                if (item.data.actionUrl != null) holder.ivHeaderRight5.visible() else holder.ivHeaderRight5.gone()
                if (item.data.follow != null) holder.tvHeaderAttention5.visible() else holder.tvHeaderAttention5.gone()
                holder.tvHeaderAttention5.setOnClickListener {
                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN)
                        .navigation()
                }
                setOnClickListener(
                    holder.tvHeaderTitle5,
                    holder.ivHeaderRight5
                ) { ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text) }
            }
            is Header7ViewHolder -> {
                holder.tvHeaderTitle7.text = item.data.text
                holder.tvHeaderAttention7.text = item.data.rightText
                setOnClickListener(holder.tvHeaderAttention7, holder.ivHeaderRight7) {
                    ActionUrlUtils.process(
                        fragment,
                        item.data.actionUrl,
                        "${item.data.text},${item.data.subTitle}"
                    )
                }
            }
            is Header8ViewHolder -> {
                holder.tvHeaderTitle8.text = item.data.text
                holder.tvHeaderAttention8.text = item.data.rightText
                setOnClickListener(holder.tvHeaderAttention8, holder.ivHeaderRight8) {
                    ActionUrlUtils.process(
                        fragment,
                        item.data.actionUrl,
                        item.data.text
                    )
                }
            }
            is Footer2ViewHolder -> {
                holder.tvFooterAttention2.text = item.data.text
                setOnClickListener(
                    holder.tvFooterAttention2,
                    holder.ivFooterRight2
                ) { ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text) }
            }
            is Footer3ViewHolder -> {
                holder.tvFooterAttention3.text = item.data.text
                setOnClickListener(
                    holder.tvFooterRefresh3,
                    holder.tvFooterAttention3,
                    holder.ivFooterRight3
                ) {
                    if (this == holder.tvFooterRefresh3) {
                        ToastUtils.show("${holder.tvFooterRefresh3.text},${GlobalUtils.getString(R.string.currently_not_supported)}")
                    } else if (this == holder.tvFooterAttention3 || this == holder.ivFooterRight3) {
                        ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
            }
            is Banner3ViewHolder -> {
                holder.bannerIvPicture3.load(item.data.image, 4f)
                holder.bannerIvAvatar3.load(item.data.header.icon)
                holder.bannerTvTitle3.text = item.data.header.title
                holder.bannerTvDes3.text = item.data.header.description
                if (item.data.label?.text.isNullOrEmpty()) holder.bannerTvAdvert3.invisible() else holder.bannerTvAdvert3.visible()
                holder.bannerTvAdvert3.text = item.data.label?.text ?: ""
                holder.itemView.setOnClickListener {
                    ActionUrlUtils.process(
                        fragment,
                        item.data.actionUrl,
                        item.data.header.title
                    )
                }
            }
            is FollowCardViewHolder -> {
                holder.followCardIvPicture.load(item.data.content.data.cover.feed, 4f)
                holder.followCardIvAvatar.load(item.data.header.icon)
                holder.followCardTvTime.text =
                    DateTimeUtils.formatMillSecond(item.data.content.data.duration.toLong())
                holder.followCardTvDes.text = item.data.header.description
                holder.followCardTvTitle.text = item.data.header.title
                if (item.data.content.data.ad) holder.followCardTvAdvert.visible() else holder.followCardTvAdvert.gone()
                if (item.data.content.data.library == DAILY_LIBRARY_TYPE) holder.followCardIvChoice.visible() else holder.followCardIvChoice.gone()
                holder.followCardIvShare.setOnClickListener {
                    fragment.activity?.let { it1 ->
                        showDialogShare(
                            it1,
                            "${item.data.content.data.title}：${item.data.content.data.webUrl.raw}"
                        )
                    }
                }
                holder.itemView.setOnClickListener {
                    item.data.content.data.run {
                        if (ad || author == null) {

                            ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                                .withLong(Const.ExtraTag.EXTRA_VIDEO_ID, id.toLong())
                                .navigation()
                        } else {
                           val videoInfo= NewDetailViewModel.VideoInfo(
                               id.toLong(),
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
                            ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                                .withParcelable(Const.ExtraTag.EXTRA_VIDEOINFO,videoInfo)
                                .navigation()
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


    companion object {
        const val TAG = "DailyAdapter"
        const val DEFAULT_LIBRARY_TYPE = "DEFAULT"
        const val NONE_LIBRARY_TYPE = "NONE"
        const val DAILY_LIBRARY_TYPE = "DAILY"
        val diff = object : DiffUtil.ItemCallback<HomeDailyBean.Item>() {
            override fun areItemsTheSame(
                oldItem: HomeDailyBean.Item,
                newItem: HomeDailyBean.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HomeDailyBean.Item,
                newItem: HomeDailyBean.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}