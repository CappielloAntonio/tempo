package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class PodcastEpisode : Parcelable {
    var id: String? = null

    @SerializedName("parent")
    var parentId: String? = null

    @SerializedName("isDir")
    var isDir = false
    var title: String? = null
    var album: String? = null
    var artist: String? = null
    var track: Int? = null
    var year: Int? = null
    var genre: String? = null

    @SerializedName("coverArt")
    var coverArtId: String? = null
    var size: Long? = null
    var contentType: String? = null
    var suffix: String? = null
    var transcodedContentType: String? = null
    var transcodedSuffix: String? = null
    var duration: Int? = null
    @ColumnInfo("bitrate")
    @SerializedName("bitRate")
    var bitrate: Int? = null
    var path: String? = null

    @ColumnInfo(name = "is_video")
    @SerializedName("isVideo")
    var isVideo: Boolean = false
    var userRating: Int? = null
    var averageRating: Double? = null
    var playCount: Long? = null
    var discNumber: Int? = null
    var created: Date? = null
    var starred: Date? = null
    var albumId: String? = null
    var artistId: String? = null
    var type: String? = null
    var bookmarkPosition: Long? = null
    var originalWidth: Int? = null
    var originalHeight: Int? = null
    var streamId: String? = null
    var channelId: String? = null
    var description: String? = null
    var status: String? = null
    var publishDate: Date? = null
}