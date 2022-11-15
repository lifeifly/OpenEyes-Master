package com.example.library_network.bean

import androidx.room.*
import com.example.library_network.room.typeconverter.ItemListConverter
import com.example.library_network.room.typeconverter.ListPlayInfoConverter
import com.example.library_network.room.typeconverter.ListTagConverter
import com.example.library_network.room.typeconverter.ListUrlConverter

/**
 *description : <p>
 *社区-关注
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 13
 */
@TypeConverters(ItemListConverter::class)
@Entity(tableName = "ca_table")
data class CommunityAttentionBean(
    val itemList: List<Item>,
    val count: Int,
    @PrimaryKey val total: Int,
    val nextPageUrl: String?,
    val adExist: Boolean
) {

    @Entity(tableName = "ca_item")
    data class Item(
        @Embedded val `data`: Data,
        val type: String,
        @Ignore val tag: Any? = null,
        @PrimaryKey val id: Int = 0,
        val adIndex: Int
    )

    @Entity(tableName = "ca_item_data")
    data class Data(
        @Ignore val adTrack: List<Any>? = null,
        @Embedded val content: Content,
        val dataType: String,
        @Embedded val header: Header,
        @PrimaryKey val type: String
    )


    @Entity(tableName = "ca_item_data_header")
    data class Header(
        @ColumnInfo(name = "header_actionurl")  val actionUrl: String,
        val followType: String,
        val icon: String?,
        val iconType: String,
        @ColumnInfo(name = "header_id") val id: Int,
        val issuerName: String,
        @Ignore val labelList: Any? = null,
        val showHateVideo: Boolean,
        @PrimaryKey val tagId: Int,
        @Ignore val tagName: Any? = null,
        val time: Long,
        val topShow: Boolean
    )
}

@Entity(tableName = "ca_item_data_content")
data class Content(
    @ColumnInfo(name="content_adindex")  val adIndex: Int,
    @Embedded val `data`: FollowCard,
    @PrimaryKey  @ColumnInfo(name = "content_id") val id: Int,
    @Ignore val tag: Any? = null,
    val type: String
)
@TypeConverters(ListPlayInfoConverter::class,ListTagConverter::class)
@Entity(tableName = "ca_item_data_content_followcard")
data class FollowCard(
    val ad: Boolean,
    @Ignore val adTrack: List<Any>? = null,
    @Embedded val author: Author?,
    @Ignore val brandWebsiteInfo: Any? = null,
    @Ignore val campaign: Any? = null,
    val category: String,
    val collected: Boolean,
    @Embedded val consumption: Consumption,
    @Embedded val cover: Cover,
    val dataType: String,
    @PrimaryKey  val date: Long,
    val description: String,
    val descriptionEditor: String,
    @Ignore val descriptionPgc: Any? = null,
    val duration: Int,
    @Ignore val favoriteAdTrack: Any? = null,
    @ColumnInfo(name = "followcard_id") val id: Long,
    val idx: Int,
    val ifLimitVideo: Boolean,
    @Ignore val label: Any? = null,
    @Ignore val labelList: List<Any>? = null,
    @Ignore val lastViewTime: Any? = null,
    val library: String,
    @Embedded val playInfo: List<PlayInfo>,
    val playUrl: String,
    val played: Boolean,
    @Ignore val playlists: Any? = null,
    @Ignore val promotion: Any? = null,
    @Embedded val provider: Provider,
    val reallyCollected: Boolean,
    val releaseTime: Long?,
    val remark: String,
    val resourceType: String,
    val searchWeight: Int,
    @Ignore val shareAdTrack: Any? = null,
    @Ignore val slogan: Any? = null,
    @Ignore val src: Any? = null,
    @Ignore val subtitles: List<Any>? = null,
    @Embedded val tags: List<Tag>,
    @Ignore val thumbPlayUrl: Any? = null,
    @Embedded val title: String,
    @Ignore val titlePgc: Any? = null,
    @Embedded val type: String,
    @Ignore val waterMarks: Any? = null,
    @Ignore val webAdTrack: Any? = null,
    @Embedded val webUrl: WebUrl
)

@TypeConverters(ListUrlConverter::class)
@Entity(tableName = "playinfo")
data class PlayInfo(
    val height: Int,
    val name: String,
    val type: String,
    @ColumnInfo(name = "playinfo_url") val url: String,
    val urlList: List<Url>,
    val width: Int
)

@Entity(tableName = "url")
data class Url(@PrimaryKey @ColumnInfo(name="url_name")val name: String, val size: Int, @ColumnInfo(name = "url_url") val url: String)

@Entity(tableName = "label")
data class Label(
    @ColumnInfo(name="label_actionurl") val actionUrl: String?,
    val text: String?,
    @PrimaryKey  val card: String,
    @Ignore val detail: Any? = null
)
