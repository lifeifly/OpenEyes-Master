package com.example.librery_base.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 16
 */
abstract class BaseRvAdapter<T,VH:RecyclerView.ViewHolder>(diffUtil: DiffUtil.ItemCallback<T>):ListAdapter<T,VH>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
       return createVH(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindVH(holder,position)
    }

    override fun getItemViewType(position: Int): Int {
        return getItemVT(position)
    }

    abstract fun getItemVT(position: Int): Int

    abstract fun bindVH(holder: RecyclerView.ViewHolder, position: Int)

    abstract fun createVH(parent: ViewGroup, viewType: Int): VH
}