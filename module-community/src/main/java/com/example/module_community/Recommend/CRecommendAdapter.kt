package com.example.module_community.Recommend

import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.example.library_common.Const
import com.example.library_common.global.InjectViewModel
import com.example.library_common.recyclerview.EmptyViewHolder
import com.example.library_common.recyclerview.RecyclerViewHelper
import com.example.library_common.utils.ActionUrlUtils
import com.example.library_common.utils.IntentDataHolderUtils
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.invisible
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.global.dp2px
import com.example.librery_base.global.setDrawable
import com.example.librery_base.recyclerview.BaseRvAdapter
import com.example.librery_base.router.RouterActivityPath
import com.example.librery_base.utils.ToastUtils
import com.example.module_community.BuildConfig
import com.example.module_community.R
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import kotlinx.coroutines.GlobalScope

/**
 *description : <p>
 *社区-推荐的适配器
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 21
 */
class CRecommendAdapter(
    private val fragment: CRecommendFragment,
    private val dataList: List<CommunityCommedBean.Item>
) : BaseRvAdapter<CommunityCommedBean.Item, RecyclerView.ViewHolder>(diff) {

    override fun getItemCount(): Int = dataList.size

    override fun getItemVT(position: Int): Int = when (dataList[position].type) {
        STR_HORIZONTAL_SCROLLCARD_TYPE -> {
            when (dataList[position].data.dataType) {
                STR_ITEM_COLLECTION_DATA_TYPE -> HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE
                STR_HORIZONTAL_SCROLLCARD_DATA_TYPE -> HORIZONTAL_SCROLLCARD_TYPE
                else -> Const.ItemViewType.UNKNOWN
            }
        }
        STR_COMMUNITY_COLUMNS_CARD -> {
            if (dataList[position].data.dataType == STR_FOLLOW_CARD_DATA_TYPE) FOLLOW_CARD_TYPE
            else Const.ItemViewType.UNKNOWN
        }
        else -> Const.ItemViewType.UNKNOWN
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE -> {
                HorizontalScrollItemCollectionViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_horizontal_scroll_collection, parent, false
                    )
                )
            }
            HORIZONTAL_SCROLLCARD_TYPE -> {
                HorizontalScrollCardViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_horizontal_scroll_card_type, parent, false
                    )
                )
            }
            FOLLOW_CARD_TYPE -> {
                CommunityFollowCardViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_community_follow_card, parent, false
                    )
                )
            }
            else -> {
                EmptyViewHolder(View(parent.context))
            }
        }
    }

    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is HorizontalScrollItemCollectionViewHolder -> {
                //占满整行
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.recyclerView.layoutManager = LinearLayoutManager(fragment.activity).apply {
                    //横向
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(
                        SquareCardOfCommunityContentItemDecoration(
                            fragment
                        )
                    )
                }
                holder.recyclerView.adapter =
                    SquareCardOfCommunityContentItemAdapter(fragment, item.data.itemList)
            }
            is HorizontalScrollCardViewHolder -> {
                //占满整行
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.viewPager.run {
                    //不循环
                    setCanLoop(false)
                    //设置圆角
                    setRoundCorner(dp2px(4F))
                    //设置左右两页的显示宽度
                    setRevealWidth(0, GlobalUtils.getDimension(R.dimen.listSpaceSize))
                    if (item.data.itemList.size == 1) setPageMargin(0) else setPageMargin(dp2px(4F))
                    //指示器不可见
                    setIndicatorVisibility(View.GONE)
                    removeDefaultPageTransformer()
                    setAdapter(BannerAdapter())
                    setOnPageClickListener { position ->
                        ActionUrlUtils.process(
                            fragment, item.data.itemList[position].data.actionUrl,
                            item.data.itemList[position].data.title
                        )
                    }
                    create(item.data.itemList)
                }
            }
            is CommunityFollowCardViewHolder -> {
                holder.tvChoicness.gone()
                holder.ivPlay.gone()
                holder.ivLayers.gone()

                if (item.data.content.data.library == "DAILY") holder.tvChoicness.visible()

                if (item.data.header?.iconType ?: "".trim() == "round") {
                    holder.ivAvatar.invisible()
                    holder.ivRoundAvatar.visible()
                    holder.ivRoundAvatar.load(item.data.content.data.owner.avatar)
                } else {
                    holder.ivAvatar.visible()
                    holder.ivAvatar.load(item.data.content.data.owner.avatar)
                    holder.ivRoundAvatar.gone()
                }

                holder.ivPicture.run {
                    val imageHeight = calculateImageHeight(
                        item.data.content.data.width,
                        item.data.content.data.height
                    )
                    layoutParams.width = fragment.maxImageWidth
                    layoutParams.height = imageHeight
                    this.load(item.data.content.data.cover.feed, 4F)
                }

                holder.tvCollect.text =
                    item.data.content.data.consumption.collectionCount.toString()
                val drawable = ContextCompat.getDrawable(
                    fragment.requireContext(),
                    R.drawable.ic_favorite_border_black_20dp
                )
                holder.tvCollect.setDrawable(drawable, 17F, 17F, 2)

                holder.tvDes.text = item.data.content.data.description
                holder.tvName.text = item.data.content.data.owner.nickname

                when (item.data.content.type) {
                    STR_UGC_PICTURE_TYPE -> {
                        //是图片类型
                        if (!item.data.content.data.urls.isNullOrEmpty() && item.data.content.data.urls?.size?:0 > 1) holder.ivLayers.visible()
                        holder.itemView.setOnClickListener {
                            val items =
                                dataList.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                            //设置数据
                            IntentDataHolderUtils.setData(EXTRA_RECOMMEND_ITEM_LIST_JSON, items)
                            IntentDataHolderUtils.setData(EXTRA_RECOMMEND_ITEM_JSON, item)
                            ARouter.getInstance().build(RouterActivityPath.Ugc.PAGER_USCDETAIL)
                                .withTransition(R.anim.anl_push_bottom_in,R.anim.anl_push_bottom_out)
                                .navigation()
                        }
                    }
                    STR_VIDEO_TYPE -> {
                        //是视频类型
                        holder.ivPlay.visible()
                        holder.itemView.setOnClickListener {
                            val items=dataList.filter { it.type== STR_COMMUNITY_COLUMNS_CARD&&it.data.dataType== STR_FOLLOW_CARD_DATA_TYPE}
                            //设置数据
                            IntentDataHolderUtils.setData(EXTRA_RECOMMEND_ITEM_LIST_JSON, items)
                            IntentDataHolderUtils.setData(EXTRA_RECOMMEND_ITEM_JSON, item)
                            ARouter.getInstance().build(RouterActivityPath.Ugc.PAGER_USCDETAIL)
                                .withTransition(R.anim.anl_push_bottom_in,R.anim.anl_push_bottom_out)
                                .navigation()
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

    /**
     * 根据图片比例计算图片高度
     * @param width Int原始宽度
     * @param height Int原始高度
     * @return Int 计算后的高度
     */
    private fun calculateImageHeight(width: Int, height: Int): Int {
        if (width == 0 || height == 0) {
            Log.w(TAG, GlobalUtils.getString(R.string.image_size_error))
            return fragment.maxImageWidth
        }
        return fragment.maxImageWidth * height / width
    }

    inner class SquareCardOfCommunityContentItemAdapter(
        val fragment: CRecommendFragment,
        var dataList: List<CommunityCommedBean.ItemX>
    ) : RecyclerView.Adapter<ItemHorizontalScrollCollectionViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemHorizontalScrollCollectionViewHolder {
            return ItemHorizontalScrollCollectionViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_horizontal_scroll_collection_item, parent, false)
            )
        }

        override fun onBindViewHolder(
            holder: ItemHorizontalScrollCollectionViewHolder,
            position: Int
        ) {
            val item = dataList[position]
            holder.tvTitle.text = item.data.title
            holder.ivBg.layoutParams.width = fragment.maxImageWidth
            holder.ivBg.load(item.data.bgPicture)
            holder.tvSubTitle.text = item.data.subTitle
            holder.itemView.setOnClickListener {
                ActionUrlUtils.process(
                    fragment,
                    item.data.actionUrl,
                    item.data.title
                )
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

    }

    inner class SquareCardOfCommunityContentItemDecoration(val fragment: CRecommendFragment) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val count = parent.adapter?.itemCount
            when (position) {
                //第一个
                0 -> {
                    outRect.right = fragment.middleSpace
                }
                //最后一个
                count?.minus(1) -> {
                    outRect.left = fragment.middleSpace
                }
                //中间的
                else -> {
                    outRect.left = fragment.middleSpace
                    outRect.right = fragment.middleSpace
                }
            }
        }
    }

    /**
     * 社区整个垂直列表的间隙
     * @property fragment CRecommendFragment
     * @constructor
     */
    class ItemDecoration(val fragment: CRecommendFragment):RecyclerView.ItemDecoration(){
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            //列数
            val spanIndex=(view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            Log.d(TAG, "getItemOffsets: 1"+spanIndex)
            outRect.top=fragment.bothSideSpace

            when(spanIndex){
                0->{
                    //第1列
                    outRect.left=fragment.bothSideSpace
                    outRect.right=fragment.middleSpace
                }
                else->{
                    //第二列
                    outRect.left=fragment.middleSpace
                    outRect.right=fragment.bothSideSpace
                }
            }
        }
    }
    companion object {
        const val TAG = "CRecommendAdapter"

        const val STR_HORIZONTAL_SCROLLCARD_TYPE = "horizontalScrollCard"
        const val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"

        const val STR_HORIZONTAL_SCROLLCARD_DATA_TYPE = "HorizontalScrollCard"
        const val STR_ITEM_COLLECTION_DATA_TYPE = "ItemCollection"
        const val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"

        const val STR_VIDEO_TYPE = "video"
        const val STR_UGC_PICTURE_TYPE = "ugcPicture"

        const val HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE =
            1   //type:horizontalScrollCard -> dataType:ItemCollection
        const val HORIZONTAL_SCROLLCARD_TYPE =
            2                   //type:horizontalScrollCard -> dataType:HorizontalScrollCard
        const val FOLLOW_CARD_TYPE =
            3                             //type:communityColumnsCard -> dataType:FollowCard
        const val EXTRA_RECOMMEND_ITEM_LIST_JSON = "recommend_item_list"
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommend_item"

        val diff = object : DiffUtil.ItemCallback<CommunityCommedBean.Item>() {
            override fun areItemsTheSame(
                oldItem: CommunityCommedBean.Item,
                newItem: CommunityCommedBean.Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CommunityCommedBean.Item,
                newItem: CommunityCommedBean.Item
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}