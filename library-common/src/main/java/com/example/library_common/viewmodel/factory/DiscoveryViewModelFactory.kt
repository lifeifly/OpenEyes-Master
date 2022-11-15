package com.example.library_common.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library_common.viewmodel.NewDetailViewModel
import com.example.library_common.viewmodel.home.DiscoveryViewModel
import com.example.library_network.GlobalNetwork

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/28 00
 */
class DiscoveryViewModelFactory(private val globalNetwork: GlobalNetwork):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DiscoveryViewModel(globalNetwork) as T
    }
}