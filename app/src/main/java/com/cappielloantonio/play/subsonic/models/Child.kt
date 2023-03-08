package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class Child(
    @PrimaryKey
    @ColumnInfo(name = "id")
    open val id: String,

    @ColumnInfo(name = "parent_id")
    @SerializedName("parent")
    var parentId: String? = null,

    @ColumnInfo(name = "is_dir")
    var isDir: Boolean = false,

    @ColumnInfo
    var title: String? = null,

    @ColumnInfo
    var album: String? = null,

    @ColumnInfo
    var artist: String? = null,

    @ColumnInfo
    var track: Int? = null,

    @ColumnInfo
    var year: Int? = null,

    @ColumnInfo
    @SerializedName("genre")
    var genre: String? = null,

    @ColumnInfo(name = "cover_art_id")
    @SerializedName("coverArt")
    var coverArtId: String? = null,

    @ColumnInfo
    var size: Long? = null,

    @ColumnInfo(name = "content_type")
    var contentType: String? = null,

    @ColumnInfo
    var suffix: String? = null,

    @ColumnInfo("transcoding_content_type")
    var transcodedContentType: String? = null,

    @ColumnInfo(name = "transcoded_suffix")
    var transcodedSuffix: String? = null,

    @ColumnInfo
    var duration: Int? = null,

    @ColumnInfo("bitrate")
    @SerializedName("bitRate")
    var bitrate: Int? = null,

    @ColumnInfo
    var path: String? = null,

    @ColumnInfo(name = "is_video")
    @SerializedName("isVideo")
    var isVideo: Boolean = false,

    @ColumnInfo(name = "user_rating")
    var userRating: Int? = null,

    @ColumnInfo(name = "average_rating")
    var averageRating: Double? = null,

    @ColumnInfo(name = "play_count")
    var playCount: Long? = null,

    @ColumnInfo(name = "disc_number")
    var discNumber: Int? = null,

    @ColumnInfo
    var created: Date? = null,

    @ColumnInfo
    var starred: Date? = null,

    @ColumnInfo(name = "album_id")
    var albumId: String? = null,

    @ColumnInfo(name = "artist_id")
    var artistId: String? = null,

    @ColumnInfo
    var type: String? = null,

    @ColumnInfo(name = "bookmark_position")
    var bookmarkPosition: Long? = null,

    @ColumnInfo(name = "original_width")
    var originalWidth: Int? = null,

    @ColumnInfo(name = "original_height")
    var originalHeight: Int? = null
) : Parcelable