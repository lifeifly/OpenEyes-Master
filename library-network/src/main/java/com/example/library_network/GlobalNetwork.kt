package com.example.library_network

import com.example.library_network.api.MainPageService
import com.example.library_network.api.VideoService

/**
 *description : <p>
 *全局网络请求工具类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 14
 */
class GlobalNetwork private constructor(){
    //首页请求的service
    private val mainPageService = ServiceCreator.createService(MainPageService::class.java)

    //视频的service
    private val videoService = ServiceCreator.createService(VideoService::class.java)

    //首页的数据
    suspend fun fetchHomeDiscovery(url: String) = mainPageService.getHomeDiscovery(url)
    suspend fun fetchHomeRecommend(url: String) = mainPageService.getHomeCommend(url)
    suspend fun fetchHomeDaily(url: String) = mainPageService.getHomeDaily(url)
    suspend fun fetchCommunityRecommend(url: String) = mainPageService.getCommunityRecommend(url)
    suspend fun fetchCommunityAttention(url: String) = mainPageService.getCommunityAttention(url)
    suspend fun fetchNotificationPush(url: String) = mainPageService.getNotificationPush(url)
    suspend fun fetchHotSearch() = mainPageService.getHotSearch()

    //视频的数据
    suspend fun fetchVideoInfo(id: Long) = videoService.getVideoInfo(id)
    suspend fun fetchVideoReplies(url: String) = videoService.getVideoReplies(url)
    suspend fun fetchVideoRecommend(id: Long) = videoService.getVideoRecommed(id)

    companion object {
        private var network: GlobalNetwork? = null
        fun getInstance(): GlobalNetwork {
            if (network == null) {
                synchronized(GlobalNetwork::class.java) {
                    if (network==null){
                        network= GlobalNetwork()
                    }
                }
            }
            return network!!
        }
    }
}