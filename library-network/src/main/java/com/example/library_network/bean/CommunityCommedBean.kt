package com.example.library_network.bean

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 *description : <p>
 *社区-推荐
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 12
 */
@Entity(tableName = "cr_table")
data class CommunityCommedBean(
    @Embedded val itemList: List<Item>,
    val count: Int,
    val total: Int,
    val nextPageUrl: String?,
    val adExist: Boolean
) {

    data class Item(
        @Embedded val `data`: Data,
        val type: String,
        @Ignore val tag: Any?,
        val id: Int = 0,
        val adIndex: Int
    )

    data class Data(
        @Ignore  val adTrack: Any,
        @Embedded val content: Content,
        val count: Int,
        val dataType: String,
        @Ignore  val footer: Any,
        @Embedded val header: Header?,
        @Embedded val itemList: List<ItemX>
    )

    data class Header(
        val actionUrl: String,
        val followType: String,
        val icon: String,
        val iconType: String,
        val id: Int,
        val issuerName: String,
        @Ignore val labelList: Any,
        val showHateVideo: Boolean,
        val tagId: Int,
        @Ignore val tagName: Any,
        val time: Long,
        val topShow: Boolean
    )

    data class Content(
        val adIndex: Int,
        @Embedded val `data`: DataX,
        val id: Int,
        @Ignore  val tag: Any,
        val type: String
    )

    data class ItemX(
        @Embedded val `data`: DataXX,
        val type: String,
        @Ignore val tag: Any?,
        val id: Int = 0,
        val adIndex: Int
    )

    data class DataX(
        val addWatermark: Boolean,
        val area: String,
        val checkStatus: String,
        val city: String,
        val collected: Boolean,
        @Embedded val consumption: Consumption,
        @Embedded val cover: Cover,
        val createTime: Long,
        val dataType: String,
        val description: String,
        val duration: Int,
        val height: Int,
        val id: Long,
        val ifMock: Boolean,
        val latitude: Double,
        val library: String,
        val longitude: Double,
        @Embedded val owner: Owner,
        val playUrl: String,
        val playUrlWatermark: String,
        val privateMessageActionUrl: String,
        val reallyCollected: Boolean,
        @Embedded val recentOnceReply: RecentOnceReply,
        val releaseTime: Long,
        val resourceType: String,
        val selectedTime: Long,
        @Ignore  val status: Any,
        @Embedded val tags: List<Tag>?,
        val title: String,
        @Ignore val transId: Any,
        val type: String,
        val uid: Int,
        val updateTime: Long,
        val url: String,
        @Embedded val urls: List<String>?,
        @Embedded val urlsWithWatermark: List<String>,
        val validateResult: String,
        val validateStatus: String,
        val validateTaskId: String,
        val width: Int
    )

    data class Owner(
        val actionUrl: String,
        @Ignore val area: Any,
        val avatar: String,
        val birthday: Long,
        val city: String,
        val country: String,
        val cover: String,
        val description: String,
        val expert: Boolean,
        val followed: Boolean,
        val gender: String,
        val ifPgc: Boolean,
        val job: String,
        val library: String,
        val limitVideoOpen: Boolean,
        val nickname: String,
        val registDate: Long,
        val releaseDate: Long,
        val uid: Int,
        val university: String,
        val userType: String
    )

    data class RecentOnceReply(
        val actionUrl: String,
        @Ignore  val contentType: Any,
        val dataType: String,
        val message: String,
        val nickname: String
    )

    data class DataXX(
        val actionUrl: String?,
        @Ignore val adTrack: List<Any>,
        val autoPlay: Boolean,
        val bgPicture: String,
        val dataType: String,
        val description: String,
        @Embedded val header: HeaderX?,
        val id: Int,
        val image: String,
        val label: Label?,
        @Ignore val labelList: List<Any>,
        val shade: Boolean,
        val subTitle: String,
        val title: String
    )

    data class HeaderX(
        @Ignore val actionUrl: Any,
        @Ignore val cover: Any,
        @Ignore val description: Any,
        @Ignore val font: Any,
        @Ignore val icon: Any,
        val id: Int,
        @Ignore val label: Any,
        @Ignore val labelList: Any,
        @Ignore val rightText: Any,
        @Ignore val subTitle: Any,
        @Ignore val subTitleFont: Any,
        val textAlign: String,
        @Ignore  val title: Any
    )
}