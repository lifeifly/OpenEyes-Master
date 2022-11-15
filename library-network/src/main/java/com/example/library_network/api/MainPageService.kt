package com.example.library_network.api

import com.example.library_network.ServiceCreator
import com.example.library_network.bean.*
import retrofit2.http.GET
import retrofit2.http.Url

/**
 *description : <p>
 *首页的数据获取api 主要包含：（首页，社区，通知，我的）对应的 API 接口。
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 12
 */
interface MainPageService {
    /**
     * 首页-发现列表
     * @param url String
     * @return HomeDiscoveryBean
     */
    @GET
    suspend fun getHomeDiscovery(@Url url: String): HomeDiscoveryBean

    /**
     * 首页-推荐
     * @param url String
     * @return HomeCommendBean
     */
    @GET
    suspend fun getHomeCommend(@Url url: String): HomeCommendBean

    /**
     * 首页-日报
     * @param url String
     * @return HomeDailyBean
     */
    @GET
    suspend fun getHomeDaily(@Url url: String): HomeDailyBean

    /**
     * 社区-推荐
     * @param url String
     * @return CommunityCommedBean
     */
    @GET
    suspend fun getCommunityRecommend(@Url url: String): CommunityCommedBean

    /**
     * 社区-关注
     * @param url String
     * @return CommunityCommedBean
     */
    @GET
    suspend fun getCommunityAttention(@Url url: String): CommunityAttentionBean

    /**
     * 通知-推送
     * @param url String
     * @return CommunityCommedBean
     */
    @GET
    suspend fun getNotificationPush(@Url url: String): NotificationPushBean

    /* * 搜索-热搜词
     * @param url String
     * @ return CommunityCommedBean
     */
    @GET("api/v3/queries/hot")
    suspend fun getHotSearch(): List<String>

    companion object {
        /**
         * 首页发现列表
         */
        const val HOME_DISCOVERY_URL = "${ServiceCreator.BASE_URL}api/v7/index/tab/discovery"



        /**
         * 首页推荐列表
         */
        const val HOME_RECOMMEND_URL = "${ServiceCreator.BASE_URL}api/v5/index/tab/allRec?page=0"

        /**
         * 首页日报列表
         */
        const val HOME_DAILY_URL = "${ServiceCreator.BASE_URL}api/v5/index/tab/feed"

        /**
         * 社区推荐列表
         */
        const val COMMUNITY_RECOMMEND_URL = "${ServiceCreator.BASE_URL}api/v7/community/tab/rec"

        /**
         * 社区关注列表
         */
        const val COMMUNITY_ATTENTION_URL = "${ServiceCreator.BASE_URL}api/v6/community/tab/follow"
        /**
         * 通知推送
         */
        const val NOTIFICATION_PUSH_URL = "${ServiceCreator.BASE_URL}api/v3/messages"
    }
}