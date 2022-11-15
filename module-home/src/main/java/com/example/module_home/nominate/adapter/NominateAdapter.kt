package com.example.module_home.nominate.adapter

import android.app.Activity
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.recyclerview.*
import com.example.library_common.utils.ActionUrlUtils
import com.example.library_common.utils.DateTimeUtils
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.library_network.bean.HomeCommendBean
import com.example.librery_base.eventbus.Navigation
import com.example.librery_base.eventbus.RefreshEvent
import com.example.librery_base.eventbus.SwitchPagesEvent
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.invisible
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.librery_base.global.setOnClickListener
import com.example.librery_base.global.showDialogShare
import com.example.librery_base.recyclerview.BaseRvAdapter
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_home.R
import com.example.module_home.daily.DailyAdapter
import com.shuyu.gsyvideoplayer.BuildConfig
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.greenrobot.eventbus.EventBus

/**
 *description : <p>
 *nominate的recyclerview的多种数据类型适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 14
 */
class NominateAdapter(
    private val fragment: Fragment,
    private val data: List<HomeCommendBean.Item>
) :
    BaseRvAdapter<HomeCommendBean.Item, RecyclerView.ViewHolder>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<HomeCommendBean.Item>() {
            override fun areItemsTheSame(
                oldItem: HomeCommendBean.Item,
                newItem: HomeCommendBean.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HomeCommendBean.Item,
                newItem: HomeCommendBean.Item
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val TAG = "CommendAdapter"


    }


    override fun getItemCount(): Int = data.size

    override fun getItemVT(position: Int): Int = RecyclerViewHelper.getItemViewType(data[position])


    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is HeaderFiveViewHolder -> {
                holder.tvHeaderTitle5.text = item.data.text
                holder.ivHeaderRight5.visibility =
                    View.VISIBLE
                holder.tvHeaderAttention5.visibility =
                    View.VISIBLE


                setOnClickListener(holder.tvHeaderTitle5, holder.ivHeaderRight5) {
                    ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text)
                }
            }
            is Header7ViewHolder -> {
                holder.tvHeaderTitle7.text = item.data.text
                holder.tvHeaderAttention7.text = item.data.rightText
                setOnClickListener(holder.tvHeaderTitle7, holder.ivHeaderRight7) {
                    ActionUrlUtils.process(
                        fragment,
                        item.data.actionUrl,
                        "${item.data.text},${item.data.rightText}"
                    )
                }
            }

            is Footer2ViewHolder -> {
                holder.tvFooterAttention2.text = item.data.text
                setOnClickListener(holder.ivFooterRight2) {
                    ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text)
                }
            }
            is Footer3ViewHolder -> {
                holder.tvFooterAttention3.text = item.data.text
                setOnClickListener(
                    holder.tvFooterRefresh3,
                    holder.ivFooterRight3,
                    holder.tvFooterAttention3
                ) {
                    if (this == holder.tvFooterRefresh3) {
                        ToastUtils.show("${holder.tvFooterRefresh3.text},${GlobalUtils.getString(R.string.currently_not_supported)}")
                    } else if (this == holder.ivFooterRight3 || this == holder.tvFooterAttention3) {
                        ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
            }
            is FollowCardViewHolder -> {
                holder.followCardIvPicture.load(item.data.content.data.cover.feed, 4F)
                holder.followCardIvAvatar.load(item.data.header.icon)
                holder.followCardTvTitle.text = item.data.header.title
                holder.followCardTvDes.text = item.data.header.description
                holder.followCardTvTime.text =
                    DateTimeUtils.formatMillSecond(item.data.content.data.duration.toLong())
                if (item.data.content.data.ad) holder.followCardTvAdvert.visibility = View.VISIBLE
                else holder.followCardTvAdvert.visibility = View.GONE
                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE)
                    holder.followCardIvChoice.visibility = View.VISIBLE
                else
                    holder.followCardIvChoice.visibility = View.GONE

                holder.followCardIvShare.setOnClickListener {
                    showDialogShare(
                        fragment.requireActivity(),
                        "${item.data.content.data.title}:${item.data.content.data.webUrl.raw}"
                    )
                }

                holder.itemView.setOnClickListener {
                    item.data.content.data.run {
                        if (ad || author == null) {
                            ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                                .withLong(
                                    Const.ExtraTag.EXTRA_VIDEO_ID,
                                    id.toLong()
                                ).navigation()
                        } else {
                            val videoInfo = NewDetailViewModel.VideoInfo(
                                id.toLong(), playUrl, title, description, category, library,
                                consumption, cover, author, webUrl
                            )
                            Log.d("EVENTBUS", "process: " + videoInfo)
                            ARouter.getInstance()
                                .build(RouterActivityPath.Detail.PAGER_DETAIL)
                                .withParcelable(
                                    Const.ExtraTag.EXTRA_VIDEOINFO,
                                    videoInfo
                                )
                                .navigation()

                        }
                    }
                }
            }
            is BannerViewHolder -> {
                holder.apply {
                    bannerIvPicture.load(item.data.icon, 4F)
                    itemView.setOnClickListener {
                        ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.title)
                    }
                }
            }
            is Banner3ViewHolder -> {
                holder.apply {
                    bannerIvPicture3.load(item.data.icon, 4F)
                    bannerIvAvatar3.load(item.data.header.icon)
                    bannerTvTitle3.text = item.data.header.title
                    bannerTvDes3.text = item.data.header.description
                    if (item.data.label?.text.isNullOrEmpty())
                        bannerTvAdvert3.invisible()
                    else
                        bannerIvAvatar3.visible()

                    bannerTvAdvert3.text = item.data.label?.text ?: ""
                    itemView.setOnClickListener {
                        ActionUrlUtils.process(
                            fragment,
                            item.data.actionUrl,
                            item.data.header.title
                        )
                    }
                }
            }

            is InfoCardViewHolder -> {
                holder.apply {
                    infoIvPicture.load(
                        item.data.icon,
                        4F,
                        RoundedCornersTransformation.CornerType.TOP
                    )
                    infoRv.setHasFixedSize(true)
                    if (infoRv.itemDecorationCount == 0) {
                        infoRv.addItemDecoration(InfoCardDecoraion())
                    }
                    infoRv.layoutManager = LinearLayoutManager(fragment.activity)
                    infoRv.adapter = InfoCardFollowCardAdapter(
                        fragment.requireActivity(), item.data.actionUrl,
                        mutableListOf()
                    )
                    itemView.setOnClickListener {
                        ActionUrlUtils.process(fragment, item.data.actionUrl)
                    }
                }
            }
            is VideoCardViewHolder -> {
                holder.apply {
                    videoCardIvPicture.load(item.data.cover.feed, 4F)
                    videoCardTvDes.text = if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE)
                        "#${item.data.category} / 开眼精选"
                    else
                        "#${item.data.category}"

                    videoCardTvTitle.text = item.data.title
                    videoCardTvTime.text = DateTimeUtils.format(item.data.duration.toLong())
                    videoCardIvShare.setOnClickListener {
                        showDialogShare(
                            fragment.requireActivity(),
                            "${item.data.title}：${item.data.webUrl.raw}"
                        )
                    }
                    itemView.setOnClickListener {
                        item.data.run {
                            ARouter.getInstance().build(RouterActivityPath.Detail.PAGER_DETAIL)
                                .withParcelable(
                                    Const.ExtraTag.EXTRA_VIDEOINFO,
                                    NewDetailViewModel.VideoInfo(
                                        id.toLong(), playUrl, title, description, category, library,
                                        consumption, cover, author, webUrl
                                    )
                                ).navigation()
                        }
                    }
                }

            }
            is UGCSelectedViewHolder -> {
                holder.apply {
                    ugcSelectedTvTitle.text = item.data.header.title
                    ugcSelectedTvRight.text = item.data.header.rightText
                    ugcSelectedTvRight.setOnClickListener {
                        EventBus.getDefault()
                            .post(SwitchPagesEvent(Navigation.Community.COMMUNITY_RECOMMEND))
                        EventBus.getDefault().post(RefreshEvent(Navigation.Community.COMMUNITY))
                    }
                    item.data.itemList.forEachIndexed { index, itemX ->
                        when (index) {
                            0 -> {
                                ugcSelectedIvLeftCover.load(
                                    itemX.data.url,
                                    4F,
                                    RoundedCornersTransformation.CornerType.LEFT
                                )
                                if (!itemX.data.urls.isNullOrEmpty() && itemX.data.urls.size > 1) ugcSelectedIvLeftLayers.visible()
                                ugcSelectedIvLeftAvatar.load(itemX.data.userCover)
                                ugcSelectedTvLeftName.text = itemX.data.nickname
                            }
                            1 -> {
                                ugcSelectedIvRTCover.load(
                                    itemX.data.url,
                                    4F,
                                    RoundedCornersTransformation.CornerType.LEFT
                                )
                                if (!itemX.data.urls.isNullOrEmpty() && itemX.data.urls.size > 1) ugcSelectedIvLeftLayers.visible()
                                ugcSelectedIvRTAvatar.load(itemX.data.userCover)
                                ugcSelectedTvRTName.text = itemX.data.nickname
                            }
                            2 -> {
                                ugcSelectedIvRBCover.load(
                                    itemX.data.url,
                                    4F,
                                    RoundedCornersTransformation.CornerType.LEFT
                                )
                                if (!itemX.data.urls.isNullOrEmpty() && itemX.data.urls.size > 1) ugcSelectedIvLeftLayers.visible()
                                ugcSelectedIvRBAvatar.load(itemX.data.userCover)
                                ugcSelectedTvRBName.text = itemX.data.nickname
                            }
                        }
                    }
                    itemView.setOnClickListener {
                        ToastUtils.show(R.string.currently_not_supported)
                    }
                }
            }
            is BreifTagCardViewHolder -> {
                holder.breifTagIvPicture.load(item.data.icon, 4f)
                holder.breifTagTvDes.text = item.data.description
                holder.breifTagTvTitle.text = item.data.title
                if (item.data.follow != null) holder.breifTagTvAttention.visible() else holder.breifTagTvAttention.gone()
                holder.breifTagTvAttention.setOnClickListener {
                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                }
                holder.itemView.setOnClickListener {
                    ToastUtils.show("${item.data.title},${GlobalUtils.getString(R.string.currently_not_supported)}")
                }
            }
            is BreifTopicCardViewHolder -> {
                holder.breifTopicIvPicture.load(item.data.icon, 4f)
                holder.breifTopicTvDes.text = item.data.description
                holder.breifTopicTvTitle.text = item.data.title
                holder.itemView.setOnClickListener {
                    ToastUtils.show("${item.data.title},${GlobalUtils.getString(R.string.currently_not_supported)}")
                }
            }
            is AutoPlayVideoViewHolder -> {
                holder.apply {
                    item.data.detail?.run {
                        autoplayIvAvatar.load(icon)
                        autoplayTvTitle.text = title
                        autoplayTvDes.text = description
                        startAutoPlay(fragment.requireActivity(),
                            autoplayGSYPlayer,
                            position,
                            url,
                            imageUrl,
                            TAG,
                            object : GSYSampleCallBack() {
                                override fun onPrepared(url: String?, vararg objects: Any?) {
                                    super.onPrepared(url, *objects)
                                    //是否需要静音
                                    GSYVideoManager.instance().isNeedMute = true
                                }

                                override fun onClickBlank(url: String?, vararg objects: Any?) {
                                    super.onClickBlank(url, *objects)
                                    ActionUrlUtils.process(fragment, actionUrl)
                                }
                            })
                        setOnClickListener(autoplayGSYPlayer.thumbImageView, itemView) {
                            ActionUrlUtils.process(fragment, actionUrl)
                        }
                    }
                }

            }
            else -> {
                holder.itemView.visibility = View.GONE
                if (BuildConfig.DEBUG) ToastUtils.show("${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}")
            }
        }
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHelper.getViewHolder(parent, viewType)
    }

    private fun startAutoPlay(
        activity: Activity,
        viewPlayer: StandardGSYVideoPlayer,
        position: Int,
        playUrl: String,
        coverUrl: String,
        tag: String,
        callBack: GSYSampleCallBack
    ) {
        viewPlayer.run {
            //防止错位设置
            playTag = tag
            //设置播放位置防止错误
            playPosition = position
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //设置循环播放
            isLooping = true
            //增加封面
            val cover = ImageView(activity)
            cover.scaleType = ImageView.ScaleType.CENTER_CROP
            cover.load(coverUrl, 4F)
            cover.parent?.run { removeView(cover) }
            thumbImageView = cover
            //设置播放过程的回调
            setVideoAllCallBack(callBack)
            //设置播放的URL
            setUp(playUrl, false, null)
        }
    }


    class InfoCardFollowCardAdapter(
        val activity: Activity,
        val actionUrl: String?,
        val dataList: List<String>
    ) : RecyclerView.Adapter<InfoCardFollowCardAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNews = view.findViewById<TextView>(R.id.item_info_card_item_tv_news)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_info_card_type_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.tvNews.text = item
            holder.itemView.setOnClickListener {
                ActionUrlUtils.process(activity, actionUrl)
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

    }


    class InfoCardDecoraion : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = parent.adapter?.itemCount
            count.run {
                when (position) {
                    0 -> {
                        outRect.top = dp2px(18F)
                    }
                    this?.minus(1) -> {
                        outRect.top = dp2px(13F)
                        outRect.bottom = dp2px(18F)
                    }
                    else -> {
                        outRect.top = dp2px(13F)
                    }
                }
            }
        }
    }

}