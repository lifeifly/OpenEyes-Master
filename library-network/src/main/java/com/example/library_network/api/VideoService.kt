package com.example.library_network.api

import com.example.library_network.ServiceCreator
import com.example.library_network.bean.VideoBeanForClient
import com.example.library_network.bean.VideoRelated
import com.example.library_network.bean.VideoReplies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 14
 */

interface VideoService {

    /**
     * 视频详情-信息
     */
    @GET("api/v2/video/{id}")
    suspend fun getVideoInfo(@Path("id") id: Long): VideoBeanForClient

    /**
     * 视频详情-推荐
     */
    @GET("api/v4/video/related")
    suspend fun getVideoRecommed(@Query("id") id: Long): VideoRelated

    /**
     * 视频详情-评论
     */
    @GET
    suspend fun getVideoReplies(@Url url: String): VideoReplies

    companion object {
        /**
         * 视频详情-评论列表的Url
         */
        const val VIDEO_REPLIES_URL = "${ServiceCreator.BASE_URL}api/v2/replies/video?videoId="
    }
}