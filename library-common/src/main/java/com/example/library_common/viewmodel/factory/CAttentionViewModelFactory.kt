package com.example.library_common.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library_common.viewmodel.community.CAttentionViewModel
import com.example.library_network.GlobalNetwork
import com.example.library_network.Repositories

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/31 20
 */
class CAttentionViewModelFactory(private val globalNetwork: Repositories):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CAttentionViewModel(globalNetwork) as T
    }
}