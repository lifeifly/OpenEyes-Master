package com.example.library_network.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *description : <p>
 *通知-推送实体类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 13
 */

data class NotificationPushBean(
    val messageList: List<Message>,
    val nextPageUrl: String?,
    val updateTime: Long
){
    data class Message(
        val actionUrl: String,
        val content: String,
        val date: Long,
        val icon: String,
        val id: Int,
        val ifPush: Boolean,
        val pushStatus: Int,
        val title: String,
        val uid: Any,
        val viewed: Boolean
    )
}

