package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Xml
open class Child(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Attribute
    open val id: String,

    @ColumnInfo(name = "parent_id")
    @Attribute(name = "parent")
    var parentId: String? = null,

    @ColumnInfo(name = "is_dir")
    @Attribute
    var isDir: Boolean = false,

    @ColumnInfo
    @Attribute
    var title: String? = null,

    @ColumnInfo
    @Attribute
    var album: String? = null,

    @ColumnInfo
    @Attribute
    var artist: String? = null,

    @ColumnInfo
    @Attribute
    var track: Int? = null,

    @ColumnInfo
    @Attribute
    var year: Int? = null,

    @ColumnInfo
    @Attribute(name = "genre")
    var genre: String? = null,

    @ColumnInfo(name = "cover_art_id")
    @Attribute(name = "coverArt")
    var coverArtId: String? = null,

    @ColumnInfo
    @Attribute
    var size: Long? = null,

    @ColumnInfo(name = "content_type")
    @Attribute
    var contentType: String? = null,

    @ColumnInfo
    @Attribute
    var suffix: String? = null,

    @ColumnInfo("transcoding_content_type")
    @Attribute
    var transcodedContentType: String? = null,

    @ColumnInfo(name = "transcoded_suffix")
    @Attribute
    var transcodedSuffix: String? = null,

    @ColumnInfo
    @Attribute
    var duration: Int? = null,

    @ColumnInfo("bitrate")
    @Attribute(name = "bitRate")
    var bitrate: Int? = null,

    @ColumnInfo
    @Attribute
    var path: String? = null,

    @ColumnInfo(name = "is_video")
    @Attribute(name = "isVideo")
    var isVideo: Boolean = false,

    @ColumnInfo(name = "user_rating")
    @Attribute
    var userRating: Int? = null,

    @ColumnInfo(name = "average_rating")
    @Attribute
    var averageRating: Double? = null,

    @ColumnInfo(name = "play_count")
    @Attribute
    var playCount: Long? = null,

    @ColumnInfo(name = "disc_number")
    @Attribute
    var discNumber: Int? = null,

    @ColumnInfo
    @Attribute(converter = DateRfc3339TypeConverter::class)
    var created: Date? = null,

    @ColumnInfo
    @Attribute(converter = DateRfc3339TypeConverter::class)
    var starred: Date? = null,

    @ColumnInfo(name = "album_id")
    @Attribute
    var albumId: String? = null,

    @ColumnInfo(name = "artist_id")
    @Attribute
    var artistId: String? = null,

    @ColumnInfo
    @Attribute
    var type: String? = null,

    @ColumnInfo(name = "bookmark_position")
    @Attribute
    var bookmarkPosition: Long? = null,

    @ColumnInfo(name = "original_width")
    @Attribute
    var originalWidth: Int? = null,

    @ColumnInfo(name = "original_height")
    @Attribute
    var originalHeight: Int? = null
) : Parcelable