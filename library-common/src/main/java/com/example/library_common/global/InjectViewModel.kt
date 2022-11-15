package com.example.library_common.global

import com.example.library_base.base.BaseApplication
import com.example.library_common.viewmodel.factory.*
import com.example.library_common.viewmodel.home.DiscoveryViewModel
import com.example.library_common.viewmodel.notification.PushViewModel
import com.example.library_network.GlobalNetwork
import com.example.library_network.Repositories

/**
 *description : <p>
 *管理viewmodel的工厂方法
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 16
 */
object InjectViewModel {
    private val globalNetwork by lazy { GlobalNetwork.getInstance() }
    private val repositories by lazy { Repositories.getInstance(BaseApplication.getInstance()) }

    fun getSearchHotViewModelFactory() = SearchViewModelFactory(globalNetwork)

    fun getHomeRecommendViewModelFactory() = NominateViewModelFactory(globalNetwork)

    fun getNewDetailViewModelFactory() = NewDetailViewModelFactory(globalNetwork)

    fun getHomeDiscoveryViewModelFactory() = DiscoveryViewModelFactory(globalNetwork)
    fun getHomeDailyViewModelFactory() = DailyViewModelFactory(globalNetwork)
    fun getCommunityRecommendViewModelFactory() = CRecommendViewModelFactory(globalNetwork)
    fun getCommunityAttentionViewModelFactory() = CAttentionViewModelFactory(repositories)
    fun getPushViewModelFactory() = PushViewModelFactory(globalNetwork)
}