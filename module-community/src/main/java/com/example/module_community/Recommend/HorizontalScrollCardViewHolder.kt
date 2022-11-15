package com.example.module_community.Recommend

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library_network.bean.CommunityCommedBean
import com.example.librery_base.extension.gone
import com.example.librery_base.extension.load
import com.example.librery_base.extension.visible
import com.example.module_community.R
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 21
 */
class HorizontalScrollCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val viewPager =
        view.findViewById<BannerViewPager<CommunityCommedBean.ItemX, ItemHorizontalScrollCardViewHolder>>(
            R.id.bannerViewPager
        )
}

class ItemHorizontalScrollCardViewHolder(view: View) :
    BaseViewHolder<CommunityCommedBean.ItemX>(view) {
    override fun bindData(data: CommunityCommedBean.ItemX?, position: Int, pageSize: Int) {
        val tvLabel = findView<TextView>(R.id.tvLabel)
        val ivPicture = findView<ImageView>(R.id.ivPicture)
        if (data?.data?.label?.text.isNullOrEmpty()) tvLabel.gone() else tvLabel.visible()
        tvLabel.text = data?.data?.label?.text ?: ""
        ivPicture.load(data?.data?.image!!, 4F)
    }
}

class BannerAdapter: BaseBannerAdapter<CommunityCommedBean.ItemX,ItemHorizontalScrollCardViewHolder>() {


    override fun createViewHolder(
        itemView: View,
        viewType: Int
    ): ItemHorizontalScrollCardViewHolder {
        return ItemHorizontalScrollCardViewHolder(itemView)
    }

    override fun onBind(
        holder: ItemHorizontalScrollCardViewHolder,
        data: CommunityCommedBean.ItemX,
        position: Int,
        pageSize: Int
    ) {
        holder.bindData(data, position, pageSize)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_horizonrtal_scroll_card_type_item
    }

}