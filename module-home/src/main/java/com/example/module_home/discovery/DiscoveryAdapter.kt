package com.example.module_home.discovery

import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.recyclerview.*
import com.example.library_common.utils.ActionUrlUtils
import com.example.library_common.utils.DateTimeUtils
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.library_network.bean.HomeDiscoveryBean
import com.example.library_video.startAutoPlay
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
import com.example.module_home.BuildConfig
import com.example.module_home.R
import com.example.module_home.daily.DailyAdapter
import com.example.module_home.nominate.adapter.NominateAdapter
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 *description : <p>
 *首页-发现的适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/29 23
 */
class DiscoveryAdapter(
    private val fragment: Fragment,
    private val dataList: List<HomeDiscoveryBean.Item>
) : BaseRvAdapter<HomeDiscoveryBean.Item, RecyclerView.ViewHolder>(diff) {


    override fun getItemCount(): Int = dataList.size

    override fun getItemVT(position: Int): Int {
        return RecyclerViewHelper.getItemViewType(dataList[position])
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHelper.getViewHolder(parent, viewType)
    }

    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is HeaderFiveViewHolder -> {
                holder.tvHeaderTitle5.text = item.data.text
                Log.d(TAG, item.data.text)
                if (item.data.actionUrl != null) holder.ivHeaderRight5.visible() else holder.ivHeaderRight5.gone()
                if (item.data.follow != null) holder.tvHeaderAttention5.visible() else holder.ivHeaderRight5.gone()
                holder.tvHeaderAttention5.setOnClickListener {
                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN).navigation()
                }
                setOnClickListener(
                    holder.tvHeaderTitle5,
                    holder.ivHeaderRight5
                ) { ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text) }
            }
            is Header7ViewHolder -> {
                holder.tvHeaderTitle7.text = item.data.text
                Log.d(TAG, item.data.text + 1)
                holder.tvHeaderAttention7.text = item.data.rightText
                setOnClickListener(holder.tvHeaderAttention7, holder.ivHeaderRight7) {
                    ActionUrlUtils.process(
                        fragment,
                        item.data.actionUrl,
                        "${item.data.text},${item.data.rightText}"
                    )
                }
            }
            is Header8ViewHolder -> {
                Log.d(TAG, item.data.text + 2)
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
                Log.d(TAG, item.data.text + 3)
                holder.tvFooterAttention2.text = item.data.text
                setOnClickListener(
                    holder.tvFooterAttention2,
                    holder.ivFooterRight2
                ) { ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text) }
            }
            is Footer3ViewHolder -> {
                Log.d(TAG, item.data.text + 4)
                holder.tvFooterAttention3.text = item.data.text
                setOnClickListener(
                    holder.tvFooterRefresh3,
                    holder.tvFooterAttention3,
                    holder.ivFooterRight3
                ) {
                    if (this == holder.tvFooterAttention3) {
                        ToastUtils.show("${holder.tvFooterRefresh3.text},${GlobalUtils.getString(R.string.currently_not_supported)}")
                    } else if (this == holder.tvFooterAttention3 || this == holder.ivFooterRight3) {
                        ActionUrlUtils.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
            }

            is HorizontalScrollViewHolder -> {
                holder.scrollVp.run {
                    setCanLoop(false)//不启用循环
                    setRoundCorner(dp2px(4f))
                    setRevealWidth(GlobalUtils.getDimension(R.dimen.listSpaceSize))
                    //就一个view就不用设置margin
                    if (item.data.itemList.size == 1) setPageMargin(0) else setPageMargin(dp2px(4f))
                    //指示器不可见
                    setIndicatorVisibility(View.GONE)
                    setAdapter(HorizontalScrollCardAdapter())
                    removeDefaultPageTransformer()
                    setOnPageClickListener { position ->
                        ActionUrlUtils.process(
                            fragment,
                            item.data.itemList[position].data.actionUrl,
                            item.data.itemList[position].data.title
                        )
                    }
                    //设置数据
                    create(item.data.itemList)
                }
            }
            is SpecialSquareViewHolder -> {
                Log.d(TAG, item.data.header.title + 7)
                holder.specialSquareTvTitle.text = item.data.header.title
                holder.specialSquareTvAttention.text = item.data.header.rightText
                setOnClickListener(
                    holder.specialSquareTvAttention,
                    holder.specialSquareIvRight
                ) { ToastUtils.show("${item.data.header.rightText},${GlobalUtils.getString(R.string.currently_not_supported)}") }
                holder.specialSquareRv.setHasFixedSize(true)
                holder.specialSquareRv.isNestedScrollingEnabled = true
                holder.specialSquareRv.layoutManager =
                    GridLayoutManager(fragment.activity, 2).apply {
                        orientation = GridLayoutManager.HORIZONTAL
                    }
                if (holder.specialSquareRv.itemDecorationCount == 0) {
                    holder.specialSquareRv.addItemDecoration(
                        SpecialSquareCardCollectionItemDecoration()
                    )
                }
                val list =
                    item.data.itemList.filter { it.type == "squareCardOfCategory" && it.data.dataType == "SquareCard" }
                holder.specialSquareRv.adapter = SpecialSquareCardCollectionAdapter(list)
            }
            is SpecialColumnViewHolder -> {
                Log.d(TAG, item.data.header.title + 8)
                holder.specialColumnTvTitle.text = item.data.header.title
                holder.specialColumnTvAttention.text = item.data.header.rightText
                setOnClickListener(
                    holder.specialColumnTvAttention,
                    holder.specialColumnIvRight
                ) { ToastUtils.show("${item.data.header.rightText},${GlobalUtils.getString(R.string.currently_not_supported)}") }
                holder.specialColumnRv.setHasFixedSize(true)
                holder.specialColumnRv.layoutManager = GridLayoutManager(fragment.activity, 2)
                if (holder.specialColumnRv.itemDecorationCount == 0) {
                    holder.specialColumnRv.addItemDecoration(
                        SpecialColumnCardCollectionItemDecoration(spanCount = 2)
                    )
                }
                val list =
                    item.data.itemList.filter { it.type == "squareCardOfColumn" && it.data.dataType == "SquareCard" }
                holder.specialColumnRv.adapter = ColumnCardListAdapter(list)
            }
            is BannerViewHolder -> {
                holder.bannerIvPicture.load(item.data.icon, 4f)
                holder.itemView.setOnClickListener {
                    ActionUrlUtils.process(
                        fragment,
                        item.data.actionUrl,
                        item.data.title
                    )
                }
            }

            is VideoCardViewHolder -> {
                Log.d(TAG, item.data.title + 11)
                holder.videoCardIvPicture.load(item.data.cover.feed, 4f)
                holder.videoCardTvDes.text =
                    if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.videoCardTvTitle.text = item.data.title
                holder.videoCardTvTime.text =
                    DateTimeUtils.formatMillSecond(item.data.duration.toLong())
                holder.videoCardIvShare.setOnClickListener {
                    fragment.activity?.let { it1 ->
                        showDialogShare(
                            it1,
                            "${item.data.title}：${item.data.webUrl.raw}"
                        )
                    }
                }
                holder.itemView.setOnClickListener {
                    item.data.run {
                        val videoIn = NewDetailViewModel.VideoInfo(
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
                            .withParcelable(Const.ExtraTag.EXTRA_VIDEOINFO, videoIn)
                            .navigation()
                    }
                }
            }
            is BreifTagCardViewHolder -> {
                Log.d(TAG, item.data.title + 12)
                holder.breifTagIvPicture.load(item.data.icon, 4f)
                holder.breifTagTvDes.text = item.data.description
                holder.breifTagTvTitle.text = item.data.title
                if (item.data.follow != null) holder.breifTagTvAttention.visible() else holder.breifTagTvAttention.gone()
                holder.breifTagTvAttention.setOnClickListener {
                    ARouter.getInstance().build(RouterActivityPath.Login.PAGER_LOGIN)
                        .navigation()
                }
                holder.itemView.setOnClickListener {
                    ToastUtils.show(
                        "${item.data.title},${
                            GlobalUtils.getString(
                                R.string.currently_not_supported
                            )
                        }"
                    )
                }
            }
            is BreifTopicCardViewHolder -> {
                Log.d(TAG, item.data.title + 13)
                holder.breifTopicIvPicture.load(item.data.icon, 4f)
                holder.breifTopicTvDes.text = item.data.description
                holder.breifTopicTvTitle.text = item.data.title
                holder.itemView.setOnClickListener {
                    ToastUtils.show(
                        "${item.data.title},${
                            GlobalUtils.getString(
                                R.string.currently_not_supported
                            )
                        }"
                    )
                }
            }
            is AutoPlayVideoViewHolder -> {
                Log.d(TAG, item.data.detail?.title + 14)
                item.data.detail?.run {
                    holder.autoplayIvAvatar.load(item.data.detail!!.icon)
                    holder.autoplayTvTitle.text = item.data.detail!!.title
                    holder.autoplayTvDes.text = item.data.detail!!.description
                    startAutoPlay(
                        fragment.requireActivity(),
                        holder.autoplayGSYPlayer,
                        position,
                        url,
                        imageUrl,
                        NominateAdapter.TAG,
                        object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                //是否静音
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                //点击空白处处理
                                ActionUrlUtils.process(fragment, item.data.detail!!.actionUrl)
                            }
                        })
                    setOnClickListener(holder.autoplayGSYPlayer.thumbImageView, holder.itemView) {
                        ActionUrlUtils.process(fragment, item.data.detail!!.actionUrl)
                    }
                }
            }
            else -> {
                holder.itemView.gone()
                if (BuildConfig.DEBUG) ToastUtils.show("${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}")
            }
        }
    }

    /**
     * ViewPager的适配器
     */
    inner class HorizontalScrollCardAdapter :
        BaseBannerAdapter<HomeDiscoveryBean.ItemX, HorizontalScrollCardAdapter.ViewHolder>() {
        inner class ViewHolder(private val view: View) :
            BaseViewHolder<HomeDiscoveryBean.ItemX>(view) {
            override fun bindData(data: HomeDiscoveryBean.ItemX?, position: Int, pageSize: Int) {
                val ivPicture = view.findViewById<ImageView>(R.id.ivPicture)
                val tvLabel = view.findViewById<TextView>(R.id.tvLabel)
                if (data?.data?.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                tvLabel.text = data?.data?.label?.text ?: ""
                data?.data?.image?.let { ivPicture.load(it, 4F) }
            }

        }

        override fun createViewHolder(itemView: View, viewType: Int): ViewHolder {
            return ViewHolder(itemView)
        }

        override fun onBind(
            holder: ViewHolder,
            data: HomeDiscoveryBean.ItemX,
            position: Int,
            pageSize: Int
        ) {
            holder.bindData(data, position, pageSize)
        }

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.item_banner_item
        }
    }

    inner class SpecialSquareCardCollectionAdapter(val dataList: List<HomeDiscoveryBean.ItemX>) :
        RecyclerView.Adapter<SpecialSquareCardCollectionAdapter.ViewHolder>() {
        inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            val ivPicture = view.findViewById<ImageView>(R.id.item_special_item_iv)
            val tvLabel = view.findViewById<TextView>(R.id.item_special_item_tv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_special_item_card, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.ivPicture.load(item.data.image, 4F)
            holder.tvLabel.text = item.data.title
            holder.itemView.setOnClickListener {
                ToastUtils.show("${item.data.title},${GlobalUtils.getString(R.string.currently_not_supported)}")
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

    }

    inner class ColumnCardListAdapter(val dataList: List<HomeDiscoveryBean.ItemX>) :
        RecyclerView.Adapter<ColumnCardListAdapter.ViewHolder>() {
        inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            val ivPicture = view.findViewById<ImageView>(R.id.item_special_column_item_iv)
            val tvTitle = view.findViewById<TextView>(R.id.item_special_column_item_tv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_special_column_item_card, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.ivPicture.load(item.data.image)
            holder.tvTitle.text = item.data.title
            holder.itemView.setOnClickListener {
                ToastUtils.show("${item.data.title},${GlobalUtils.getString(R.string.currently_not_supported)}")
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    inner class SpecialSquareCardCollectionItemDecoration: RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = parent.adapter?.itemCount
            val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            val spanCount=2
            val lastRowFirstItemPosition = count?.minus(spanCount)//最后一行，第一个item的索引
            val space = dp2px(2F)
            val rightCountSpace = dp2px(14F)
            Log.d(TAG, "getItemOffsets: " + "${position}${spanIndex}")
            when (spanIndex) {
                //第一列
                0 -> {
                    outRect.bottom = space
                }
                //最后一列
                spanCount - 1 -> {
                    outRect.top = space
                }
                //中间列
                else -> {
                    outRect.top = space
                    outRect.bottom = space
                }
            }
            when{
                //第一行
                position<spanCount->{
                    outRect.right = space
                }
                //中间行
                position<lastRowFirstItemPosition!!->{
                    outRect.left=space
                    outRect.right=space
                }
                //最后一行
                else->{
                    outRect.left=space
                    outRect.right=rightCountSpace
                }
            }
        }
    }

    inner class SpecialColumnCardCollectionItemDecoration(
        private val spanCount: Int = 2,
        private val space: Float = 2F
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = parent.adapter?.itemCount
            val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            val lastRowFirstItemPosition = count?.minus(spanCount)//最后一行，第一个item的索引
            val space = dp2px(space)
            Log.d(TAG, "getItemOffsets: " + "${position}${spanIndex}")
            when {
                //第一行
                position < spanCount -> {
                    outRect.bottom = space
                }
                //第一行-最后一行
                position < lastRowFirstItemPosition!! -> {
                    outRect.top = space
                    outRect.bottom = space
                }
                //最后你一行
                else -> {
                    outRect.top = space
                }
            }

            when (spanIndex) {
                //第一列
                0 -> {
                    outRect.right = space
                }
                //最后一列
                spanCount - 1 -> {
                    outRect.left = space
                }
                //中间列
                else -> {
                    outRect.left = space
                    outRect.right = space
                }
            }
        }
    }

    companion object {
        const val TAG = "DiscoveryAdapter"

        val diff = object : DiffUtil.ItemCallback<HomeDiscoveryBean.Item>() {
            override fun areItemsTheSame(
                oldItem: HomeDiscoveryBean.Item,
                newItem: HomeDiscoveryBean.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HomeDiscoveryBean.Item,
                newItem: HomeDiscoveryBean.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}