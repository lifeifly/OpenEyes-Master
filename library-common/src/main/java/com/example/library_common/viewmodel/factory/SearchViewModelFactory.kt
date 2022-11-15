package com.example.library_common.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.library_common.viewmodel.SearchViewModel
import com.example.library_network.GlobalNetwork

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 16
 */
class SearchViewModelFactory(private val globalNetwork: GlobalNetwork):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(globalNetwork) as T
    }
}