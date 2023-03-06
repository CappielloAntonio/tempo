package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Xml
class PodcastEpisode : Parcelable {
    @Attribute
    var id: String? = null

    @Attribute(name = "parent")
    var parentId: String? = null

    @Attribute(name = "isDir")
    var isDir = false

    @Attribute
    var title: String? = null

    @Attribute
    var album: String? = null

    @Attribute
    var artist: String? = null

    @Attribute
    var track: Int? = null

    @Attribute
    var year: Int? = null

    @Attribute(name = "genre")
    var genre: String? = null

    @Attribute(name = "coverArt")
    var coverArtId: String? = null

    @Attribute
    var size: Long? = null

    @Attribute
    var contentType: String? = null

    @Attribute
    var suffix: String? = null

    @Attribute
    var transcodedContentType: String? = null

    @Attribute
    var transcodedSuffix: String? = null

    @Attribute
    var duration: Int? = null

    @Attribute
    var bitRate: Int? = null

    @Attribute
    var path: String? = null

    @Attribute(name = "isVideo")
    var video: Boolean? = null

    @Attribute
    var userRating: Int? = null

    @Attribute
    var averageRating: Double? = null

    @Attribute
    var playCount: Long? = null

    @Attribute
    var discNumber: Int? = null

    @Attribute(converter = DateRfc3339TypeConverter::class)
    var created: Date? = null

    @Attribute(converter = DateRfc3339TypeConverter::class)
    var starred: Date? = null

    @Attribute
    var albumId: String? = null

    @Attribute
    var artistId: String? = null

    @Attribute
    var type: String? = null

    @Attribute
    var bookmarkPosition: Long? = null

    @Attribute
    var originalWidth: Int? = null

    @Attribute
    var originalHeight: Int? = null

    @Attribute
    var streamId: String? = null

    @Attribute
    var channelId: String? = null

    @Attribute
    var description: String? = null

    @Attribute
    var status: String? = null

    @Attribute(converter = DateRfc3339TypeConverter::class)
    var publishDate: Date? = null
}