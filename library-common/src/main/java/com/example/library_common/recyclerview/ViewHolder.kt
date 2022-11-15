package com.example.library_common.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.library_common.Const.ItemViewType.Companion.AUTO_PLAY_VIDEO_AD
import com.example.library_common.Const.ItemViewType.Companion.BANNER
import com.example.library_common.Const.ItemViewType.Companion.BANNER3
import com.example.library_common.Const.ItemViewType.Companion.COLUMN_CARD_LIST
import com.example.library_common.Const.ItemViewType.Companion.FOLLOW_CARD
import com.example.library_common.Const.ItemViewType.Companion.HORIZONTAL_SCROLL_CARD
import com.example.library_common.Const.ItemViewType.Companion.INFORMATION_CARD
import com.example.library_common.Const.ItemViewType.Companion.SPECIAL_SQUARE_CARD_COLLECTION
import com.example.library_common.Const.ItemViewType.Companion.TAG_BRIEFCARD
import com.example.library_common.Const.ItemViewType.Companion.TEXT_CARD_FOOTER2
import com.example.library_common.Const.ItemViewType.Companion.TEXT_CARD_FOOTER3
import com.example.library_common.Const.ItemViewType.Companion.TEXT_CARD_HEADER4
import com.example.library_common.Const.ItemViewType.Companion.TEXT_CARD_HEADER5
import com.example.library_common.Const.ItemViewType.Companion.TEXT_CARD_HEADER7
import com.example.library_common.Const.ItemViewType.Companion.TEXT_CARD_HEADER8
import com.example.library_common.Const.ItemViewType.Companion.TOPIC_BRIEFCARD
import com.example.library_common.Const.ItemViewType.Companion.UGC_SELECTED_CARD_COLLECTION
import com.example.library_common.Const.ItemViewType.Companion.UNKNOWN
import com.example.library_common.Const.ItemViewType.Companion.VIDEO_SMALL_CARD
import com.example.library_common.databinding.*
import com.example.library_network.bean.*

/**
 *description : <p>
 *通用的ViewHolder
 *</p>
 *
 *@author : flyli
 *@since :2021/5/26 20
 */

/**
 * 未知类型
 * @constructor
 */
class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
class HeaderFourViewHolder(private val binding: ItemHeaderTextcard4Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val tvHeaderTitle4 = binding.itemHeaderTvTitle4
    val ivHeaderRight4 = binding.itemHeaderIvRight4

    companion object {
        fun newInstance(parent: ViewGroup): HeaderFourViewHolder {
            val binding = ItemHeaderTextcard4Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HeaderFourViewHolder(binding)
        }
    }
}

class HeaderFiveViewHolder(private val binding: ItemHeaderTextcard5Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val tvHeaderTitle5 = binding.itemHeaderTvTitle5
    val ivHeaderRight5 = binding.itemHeaderIvRight5
    val tvHeaderAttention5 = binding.itemHeaderTvAttention5

    companion object {
        fun newInstance(parent: ViewGroup): HeaderFiveViewHolder {
            val binding = ItemHeaderTextcard5Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HeaderFiveViewHolder(binding)
        }
    }
}

class Header7ViewHolder(private val binding: ItemHeaderTextcard7Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val tvHeaderTitle7 = binding.itemHeaderTvTitle7
    val ivHeaderRight7 = binding.itemHeaderIvRight7
    val tvHeaderAttention7 = binding.itemHeaderTvAttention7

    companion object {
        fun newInstance(parent: ViewGroup): Header7ViewHolder {
            val binding = ItemHeaderTextcard7Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Header7ViewHolder(binding)
        }
    }
}

class Header8ViewHolder(private val binding: ItemHeaderTextcard8Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val tvHeaderTitle8 = binding.itemHeaderTvTitle8
    val ivHeaderRight8 = binding.itemHeaderIvRight8
    val tvHeaderAttention8 = binding.itemHeaderTvAttention8

    companion object {
        fun newInstance(parent: ViewGroup): Header8ViewHolder {
            val binding = ItemHeaderTextcard8Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Header8ViewHolder(binding)
        }
    }
}

class Footer2ViewHolder(private val binding: ItemFooterTextcard2Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val ivFooterRight2 = binding.itemIvRightFooter2
    val tvFooterAttention2 = binding.itemTvAttentionFooter2

    companion object {
        fun newInstance(parent: ViewGroup): Footer2ViewHolder {
            val binding = ItemFooterTextcard2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Footer2ViewHolder(binding)
        }
    }
}

class Footer3ViewHolder(private val binding: ItemFooterTextcard3Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val ivFooterRight3 = binding.itemIvRightFooter3
    val tvFooterAttention3 = binding.itemTvAttentionFooter3
    val refreshContainer3 = binding.flRefresh
    val tvFooterRefresh3 = binding.itemTvRefreshFooter3

    companion object {
        fun newInstance(parent: ViewGroup): Footer3ViewHolder {
            val binding = ItemFooterTextcard3Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Footer3ViewHolder(binding)
        }
    }
}

class Banner3ViewHolder(private val binding: ItemBanner3Binding) :
    RecyclerView.ViewHolder(binding.root) {
    val bannerIvPicture3 = binding.itemBanner3Iv
    val bannerIvAvatar3 = binding.itemBanner3IvAvatar
    val bannerUserInfoContainer3 = binding.userInfoContainer
    val bannerTvAdvert3 = binding.itemBanner3TvAdvert
    val bannerTvTitle3 = binding.itemBanner3TvTitle
    val bannerTvDes3 = binding.itemBanner3TvDes


    companion object {
        fun newInstance(parent: ViewGroup): Banner3ViewHolder {
            val binding = ItemBanner3Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return Banner3ViewHolder(binding)
        }
    }
}

class VideoCardViewHolder(private val binding: ItemVideoCardTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val videoCardIvPicture = binding.itemVideoCardIvPicture
    val videoCardIvShare = binding.itemVideoCardIvShare
    val videoCardTvTime = binding.itemVideoCardTvTime
    val videoCardTvTitle = binding.itemVideoCardTvTitle
    val videoCardTvDes = binding.itemVideoCardTvDes


    companion object {
        fun newInstance(parent: ViewGroup): VideoCardViewHolder {
            val binding = ItemVideoCardTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return VideoCardViewHolder(binding)
        }
    }
}

class BreifTagCardViewHolder(private val binding: ItemBriefCardTagBriefCardTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val breifTagIvPicture = binding.ivPicture
    val breifTagTvAttention = binding.tvFollow
    val breifTagTvTitle = binding.tvTitle
    val breifTagTvDes = binding.tvDescription

    companion object {
        fun newInstance(parent: ViewGroup): BreifTagCardViewHolder {
            val binding = ItemBriefCardTagBriefCardTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return BreifTagCardViewHolder(binding)
        }
    }
}

class BreifTopicCardViewHolder(private val binding: ItemBriefCardTopicBriefCardTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val breifTopicIvPicture = binding.ivPicture
    val breifTopicTvTitle = binding.tvTitle
    val breifTopicTvDes = binding.tvDescription

    companion object {
        fun newInstance(parent: ViewGroup): BreifTopicCardViewHolder {
            val binding = ItemBriefCardTopicBriefCardTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return BreifTopicCardViewHolder(binding)
        }
    }
}

class InfoCardViewHolder(private val binding: ItemInfoCardTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val infoIvPicture = binding.itemInfoCardCover
    val infoRv = binding.itemInfoCardRv

    companion object {
        fun newInstance(parent: ViewGroup): InfoCardViewHolder {
            val binding = ItemInfoCardTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return InfoCardViewHolder(binding)
        }
    }
}

class AutoPlayVideoViewHolder(private val binding: ItemAutoPlayVideoAdBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val autoplayIvAvatar = binding.itemAutoPlayIvAvatar
    val autoplayGSYPlayer = binding.videoPlayer
    val autoplayTvTitle = binding.itemAutoPlayTvTitle
    val autoplayTvDes = binding.itemAutoPlayTvDes
    val autoplayTvAdvert = binding.itemAutoPlayTvLabel


    companion object {
        fun newInstance(parent: ViewGroup): AutoPlayVideoViewHolder {
            val binding = ItemAutoPlayVideoAdBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return AutoPlayVideoViewHolder(binding)
        }
    }
}

class UGCSelectedViewHolder(private val binding: ItemUgcSelectedCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val ugcSelectedTvTitle = binding.itemUgcSelectedTvTitle
    val ugcSelectedTvRight = binding.itemUgcSelectedTvRight
    val ugcSelectedIvRight = binding.itemUgcSelectedIvRight
    val ugcSelectedIvLeftCover = binding.itemUgcSelectedIvCoverLeft
    val ugcSelectedIvLeftLayers = binding.itemUgcSelectedIvLayersLeft
    val ugcSelectedIvLeftAvatar = binding.itemUgcSelectedIvAvatarLeft
    val ugcSelectedTvLeftName = binding.itemUgcSelectedTvNameLeft
    val ugcSelectedIvRTCover = binding.itemUgcSelectedIvCoverRightTop
    val ugcSelectedIvRTLayers = binding.itemUgcSelectedIvLayersRightTop
    val ugcSelectedIvRTAvatar = binding.itemUgcSelectedIvAvatarRightTop
    val ugcSelectedTvRTName = binding.itemUgcSelectedTvNameRightTop
    val ugcSelectedIvRBCover = binding.itemUgcSelectedIvCoverRightBottom
    val ugcSelectedIvRBLayers = binding.itemUgcSelectedIvLayersRightBottom
    val ugcSelectedIvRBAvatar = binding.itemUgcSelectedIvAvatarRightBottom
    val ugcSelectedTvRBName = binding.itemUgcSelectedTvNameRightBottom


    companion object {
        fun newInstance(parent: ViewGroup): UGCSelectedViewHolder {
            val binding = ItemUgcSelectedCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return UGCSelectedViewHolder(binding)
        }
    }
}

class HorizontalScrollViewHolder(private val binding: ItemHorizontalScrollCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val scrollContainer = binding.horizontalScrollContainer
    val scrollVp = binding.horizontalScrollVp

    companion object {
        fun newInstance(parent: ViewGroup): HorizontalScrollViewHolder {
            val binding = ItemHorizontalScrollCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return HorizontalScrollViewHolder(binding)
        }
    }
}

class SpecialSquareViewHolder(private val binding: ItemSpecialSquareCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val specialSquareIvRight = binding.itemSpecialIvRight
    val specialSquareRv = binding.itemSpecialRv
    val specialSquareTvTitle = binding.itemSpecialTvTitle
    val specialSquareTvAttention = binding.itemSpecialTvAttention

    companion object {
        fun newInstance(parent: ViewGroup): SpecialSquareViewHolder {
            val binding = ItemSpecialSquareCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SpecialSquareViewHolder(binding)
        }
    }
}

class SpecialColumnViewHolder(private val binding: ItemSpecialColumnCardListBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val specialColumnIvRight = binding.itemSpecialColumnIvRight
    val specialColumnRv = binding.itemSpecialRv
    val specialColumnTvTitle = binding.itemSpecialColumnTvTitle
    val specialColumnTvAttention = binding.itemSpecialColumnTvAttention

    companion object {
        fun newInstance(parent: ViewGroup): SpecialColumnViewHolder {
            val binding = ItemSpecialColumnCardListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return SpecialColumnViewHolder(binding)
        }
    }
}

class FollowCardViewHolder(private val binding: ItemFollowCardTypeBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val followCardIvAvatar = binding.itemFollowCardIvAvatar
    val followCardIvPicture = binding.itemFollowCardIvPicture
    val followCardIvShare = binding.itemFollowCardIvShare
    val followCardIvStar = binding.itemFollowCardIvStar
    val followCardIvChoice = binding.itemFollowCardIvChoiceness
    val followCardTvTitle = binding.itemFollowCardTvTitle
    val followCardTvDes = binding.itemFollowCardTvDes
    val followCardTvTime = binding.itemFollowCardTvTime
    val followCardTvAdvert = binding.itemFollowCardTvAdvert

    companion object {
        fun newInstance(parent: ViewGroup): FollowCardViewHolder {
            val binding = ItemFollowCardTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return FollowCardViewHolder(binding)
        }
    }
}

/**
 * 帮助获取viewholder和ItemViewType
 */
object RecyclerViewHelper {
    fun getViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TEXT_CARD_FOOTER2 -> Footer2ViewHolder.newInstance(parent)
        TEXT_CARD_FOOTER3
        -> Footer3ViewHolder.newInstance(parent)
        TEXT_CARD_HEADER4
        -> HeaderFourViewHolder.newInstance(parent)
        TEXT_CARD_HEADER5
        -> HeaderFiveViewHolder.newInstance(parent)
        TEXT_CARD_HEADER7
        -> Header7ViewHolder.newInstance(parent)
        TEXT_CARD_HEADER8
        -> Header8ViewHolder.newInstance(parent)
        HORIZONTAL_SCROLL_CARD
        -> HorizontalScrollViewHolder.newInstance(parent)
        SPECIAL_SQUARE_CARD_COLLECTION
        -> SpecialSquareViewHolder.newInstance(parent)
        COLUMN_CARD_LIST
        -> SpecialColumnViewHolder.newInstance(parent)
        BANNER
        -> BannerViewHolder.newInstance(parent)
        BANNER3
        -> Banner3ViewHolder.newInstance(parent)
        VIDEO_SMALL_CARD
        -> VideoCardViewHolder.newInstance(parent)
        TAG_BRIEFCARD
        -> BreifTagCardViewHolder.newInstance(parent)
        TOPIC_BRIEFCARD
        -> BreifTopicCardViewHolder.newInstance(parent)
        FOLLOW_CARD
        -> FollowCardViewHolder.newInstance(parent)
        INFORMATION_CARD
        -> InfoCardViewHolder.newInstance(parent)
        UGC_SELECTED_CARD_COLLECTION
        -> UGCSelectedViewHolder.newInstance(parent)
        AUTO_PLAY_VIDEO_AD
        -> AutoPlayVideoViewHolder.newInstance(parent)
        else -> {
            EmptyViewHolder(View(parent.context))
        }
    }

    fun getItemViewType(item: HomeDiscoveryBean.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }


    fun getItemViewType(item: HomeCommendBean.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: HomeDailyBean.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }


    fun getItemViewType(item: CommunityAttentionBean.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.dataType) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: VideoRelated.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

    fun getItemViewType(item: VideoReplies.Item): Int {
        return if (item.type == "textCard") getTextCardType(item.data.type) else getItemViewType(
            item.type,
            item.data.dataType
        )
    }

}

class BannerViewHolder(private val binding: ItemBannerBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val bannerIvPicture = binding.itemBannerIv

    companion object {
        fun newInstance(parent: ViewGroup): BannerViewHolder {
            val binding = ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return BannerViewHolder(binding)
        }
    }
}


private fun getTextCardType(type: String) = when (type) {
    "header4" -> TEXT_CARD_HEADER4
    "header5" -> TEXT_CARD_HEADER5
    "header7" -> TEXT_CARD_HEADER7
    "header8" -> TEXT_CARD_HEADER8
    "footer2" -> TEXT_CARD_FOOTER2
    "footer3" -> TEXT_CARD_FOOTER3
    else -> UNKNOWN//未知类型
}

fun getItemViewType(type: String, dataType: String) = when (type) {
    "horizontalScrollCard" -> {
        when (dataType) {
            "HorizontalScrollCard" -> HORIZONTAL_SCROLL_CARD
            else -> UNKNOWN
        }
    }
    "specialSquareCardCollection" -> {
        when (dataType) {
            "ItemCollection" -> SPECIAL_SQUARE_CARD_COLLECTION
            else -> UNKNOWN
        }
    }

    "columnCardList" -> {
        when (dataType) {
            "ItemCollection" -> COLUMN_CARD_LIST
            else -> UNKNOWN
        }
    }
    "banner" -> {
        when (dataType) {
            "Banner" -> BANNER
            else -> UNKNOWN
        }
    }
    "banner3" -> {
        when (dataType) {
            "Banner" -> BANNER3
            else -> UNKNOWN
        }
    }
    "videoSmallCard" -> {
        when (dataType) {
            "VideoBeanForClient" -> VIDEO_SMALL_CARD
            else -> UNKNOWN
        }
    }
    "briefCard" -> {
        when (dataType) {
            "TagBriefCard" -> TAG_BRIEFCARD
            "TopicBriefCard" -> TOPIC_BRIEFCARD
            else -> UNKNOWN
        }
    }
    "followCard" -> {
        when (dataType) {
            "FollowCard" -> FOLLOW_CARD
            else -> UNKNOWN
        }
    }

    "ugcSelectedCardCollection" -> {
        when (dataType) {
            "ItemCollection" -> UGC_SELECTED_CARD_COLLECTION
            else -> UNKNOWN
        }
    }

    "informationCard" -> {
        when (dataType) {
            "ItemCollection" -> INFORMATION_CARD
            else -> UNKNOWN
        }
    }
    "autoPlayVideoAd" -> {
        when (dataType) {
            "AutoPlayVideoAdDetail" -> AUTO_PLAY_VIDEO_AD
            else -> UNKNOWN
        }
    }
    else -> {
        UNKNOWN
    }
}




