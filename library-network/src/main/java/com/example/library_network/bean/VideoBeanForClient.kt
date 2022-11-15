package com.example.library_network.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 *description : <p>
 *视频详情信息实体类
 *</p>
 *
 *@author : flyli
 *@since :2021/5/25 13
 */
data class VideoBeanForClient(
    val ad: Boolean,
    val adTrack: List<Any>,
    val author: Author,
    val brandWebsiteInfo: Any,
    val campaign: Any,
    val category: String,
    val collected: Boolean,
    val consumption: Consumption,
    val cover: Cover,
    val dataType: String,
    val date: Long,
    val description: String,
    val descriptionEditor: String,
    val descriptionPgc: String,
    val duration: Int,
    val favoriteAdTrack: Any,
    val id: Long,
    val idx: Int,
    val ifLimitVideo: Boolean,
    val label: Any,
    val labelList: List<Any>,
    val lastViewTime: Any,
    val library: String,
    val playInfo: List<Any>,
    val playUrl: String,
    val played: Boolean,
    val playlists: Any,
    val promotion: Any,
    val provider: Provider,
    val reallyCollected: Boolean,
    val recallSource: Any,
    val releaseTime: Long,
    val remark: String,
    val resourceType: String,
    val searchWeight: Int,
    val shareAdTrack: Any,
    val slogan: Any,
    val src: Any,
    val subtitles: List<Any>,
    val tags: List<Tag>,
    val thumbPlayUrl: Any,
    val title: String,
    val titlePgc: String,
    val type: String,
    val waterMarks: Any,
    val webAdTrack: Any,
    val webUrl: WebUrl
)

@Entity(tableName = "author")
@SuppressLint("ParcelCreator")
@Parcelize
data class Author(
    @Ignore val adTrack: @RawValue Any?=null,
    val approvedNotReadyVideoCount: Int,
    @ColumnInfo(name="author_description") val description: String,
    val expert: Boolean,
    @Embedded  val follow: Follow,
    val icon: String?,
    @PrimaryKey @ColumnInfo(name="author_id") val id: Int,
    val ifPgc: Boolean,
    val latestReleaseTime: Long,
    val link: String,
    @ColumnInfo(name="provider_name") val name: String,
    val recSort: Int,
    @Embedded val shield: Shield,
    val videoNum: Int
) : Parcelable {
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Entity(tableName = "author_follow")
    data class Follow(val followed: Boolean, @PrimaryKey @ColumnInfo(name = "follow_itemid") val itemId: Int, val itemType: String) : Parcelable
    @SuppressLint("ParcelCreator")
    @Parcelize
    @Entity(tableName = "author_follow")
    data class Shield(@PrimaryKey @ColumnInfo(name = "shield_itemid") val itemId: Int, val itemType: String, val shielded: Boolean) : Parcelable
}
@Entity(tableName = "provider")
data class Provider(val alias: String, val icon: String,@PrimaryKey @ColumnInfo(name="provider_name") val name: String)

@Entity(tableName = "tag")
data class Tag(
    @ColumnInfo(name="tag_actionurl") val actionUrl: String,
    @Ignore val adTrack: Any?=null,
    val bgPicture: String,
    @Ignore  val childTagIdList: Any?=null,
    @Ignore val childTagList: Any?=null,
    val communityIndex: Int,
    val desc: String,
    val haveReward: Boolean,
    val headerImage: String,
    @ColumnInfo(name = "tag_id") val id: Int,
    val ifNewest: Boolean,
    @PrimaryKey  @ColumnInfo(name="tag_name") val name: String,
    @Ignore  val newestEndTime: Any?=null,
    val tagRecType: String
)
@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = "weburl")
data class WebUrl(val forWeibo: String,@PrimaryKey val raw: String) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = "cover")
data class Cover(val blurred: String, val detail: String, @PrimaryKey val feed: String, val homepage: String?, val sharing: String?) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
@Entity(tableName = "consumption")
data class Consumption(@PrimaryKey val collectionCount: Int, val realCollectionCount: Int, val replyCount: Int, val shareCount: Int) : Parcelable

