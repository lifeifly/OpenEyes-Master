package com.example.library_common.contract

import android.os.Parcel
import android.os.Parcelable

/**
 *description : <p>
 *应用描述
 *</p>
 *
 *@author : flyli
 *@since :2021/5/22 17
 */
class VideoHeaderBean() :Parcelable {
    var videoTitle: String? = null

    var category: String? = null

    var video_description: String? = null

    // 点赞
    var collectionCount = 0

    // 分享
    var shareCount = 0

    var avatar: String? = null

    var nickName: String? = null

    var userDescription: String? = null

    var playerUrl: String? = null

    var blurredUrl: String? = null

    var videoId = 0

    constructor(parcel: Parcel) : this() {
        videoTitle = parcel.readString()
        category = parcel.readString()
        video_description = parcel.readString()
        collectionCount = parcel.readInt()
        shareCount = parcel.readInt()
        avatar = parcel.readString()
        nickName = parcel.readString()
        userDescription = parcel.readString()
        playerUrl = parcel.readString()
        blurredUrl = parcel.readString()
        videoId = parcel.readInt()
    }



    constructor(
        videoTitle: String?, category: String?,
        video_description: String?, collectionCount: Int, shareCount: Int,
        avatar: String?, nickName: String?, userDescription: String?,
        playerUrl: String?, blurredUrl: String?, videoId: Int
    ) :this(){
        this.videoTitle = videoTitle
        this.category = category
        this.video_description = video_description
        this.collectionCount = collectionCount
        this.shareCount = shareCount
        this.avatar = avatar
        this.nickName = nickName
        this.userDescription = userDescription
        this.playerUrl = playerUrl
        this.blurredUrl = blurredUrl
        this.videoId = videoId
    }

    protected fun VideoHeaderBean(`in`: Parcel) {
        videoTitle = `in`.readString()
        category = `in`.readString()
        video_description = `in`.readString()
        collectionCount = `in`.readInt()
        shareCount = `in`.readInt()
        avatar = `in`.readString()
        nickName = `in`.readString()
        userDescription = `in`.readString()
        playerUrl = `in`.readString()
        blurredUrl = `in`.readString()
        videoId = `in`.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(videoTitle)
        dest.writeString(category)
        dest.writeString(video_description)
        dest.writeInt(collectionCount)
        dest.writeInt(shareCount)
        dest.writeString(avatar)
        dest.writeString(nickName)
        dest.writeString(userDescription)
        dest.writeString(playerUrl)
        dest.writeString(blurredUrl)
        dest.writeInt(videoId)
    }

    override fun describeContents(): Int {
        return 0
    }




    override fun toString(): String {
        return "VideoHeaderBean{" +
                "videoTitle='" + videoTitle + '\'' +
                ", category='" + category + '\'' +
                ", video_description='" + video_description + '\'' +
                ", collectionCount=" + collectionCount +
                ", shareCount=" + shareCount +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userDescription='" + userDescription + '\'' +
                ", playerUrl='" + playerUrl + '\'' +
                ", blurredUrl='" + blurredUrl + '\'' +
                ", videoId=" + videoId +
                '}'
    }

    companion object CREATOR : Parcelable.Creator<VideoHeaderBean> {
        override fun createFromParcel(parcel: Parcel): VideoHeaderBean {
            return VideoHeaderBean(parcel)
        }

        override fun newArray(size: Int): Array<VideoHeaderBean?> {
            return arrayOfNulls(size)
        }
    }

}