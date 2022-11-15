package com.example.library_common.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.library_common.BR
import com.example.library_common.R
import com.example.library_common.databinding.SearchItemBinding
import com.example.library_common.databinding.SearchItemHeaderBinding
import com.example.librery_base.global.GlobalUtils
import com.example.librery_base.recyclerview.BaseRvAdapter
import com.example.librery_base.utils.ToastUtils


/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 16
 */
class SearchRvAdapter(var dataList: List<String>) :
    BaseRvAdapter<String, RecyclerView.ViewHolder>(itemCallback) {
    companion object {
        const val TAG = "HotSearchAdapter"

        val itemCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == oldItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == oldItem
            }
        }
    }

    override fun getItemVT(position: Int): Int {
        return when (position) {
            0 -> R.layout.search_item_header
            else -> R.layout.search_item
        }
    }

    override fun bindVH(holder: RecyclerView.ViewHolder, position: Int) {
        val data = dataList[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind()
            is HotViewHolder -> holder.bind(data)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.search_item_header -> HeaderViewHolder.newInstance(parent)
            else -> HotViewHolder.newInstance(parent)
        }
    }

    class HeaderViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun newInstance(parent: ViewGroup): HeaderViewHolder {
                val binding = SearchItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return HeaderViewHolder(binding)
            }
        }

        fun bind() {
            (binding as SearchItemHeaderBinding).tvTitle.text =
                GlobalUtils.getString(R.string.hot_keywords)

        }
    }

    class HotViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun newInstance(parent: ViewGroup): HotViewHolder {
                val binding = SearchItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.root.setOnClickListener { v ->
                    ToastUtils.show(R.string.currently_not_supported)
                }
                return HotViewHolder(binding)
            }
        }

        fun bind(data: String) {
            binding.setVariable(BR.searchdata, data)
            binding.executePendingBindings()
        }
    }
}
