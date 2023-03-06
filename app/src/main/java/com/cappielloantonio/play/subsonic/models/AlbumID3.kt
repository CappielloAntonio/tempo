package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Xml(name = "album")
open class AlbumID3 : Parcelable {
    @Attribute
    var id: String? = null

    @Attribute
    var name: String? = null

    @Attribute
    var artist: String? = null

    @Attribute
    var artistId: String? = null

    @Attribute(name = "coverArt")
    var coverArtId: String? = null

    @Attribute
    var songCount = 0

    @Attribute
    var duration = 0

    @Attribute
    var playCount: Long? = null

    @Attribute(converter = DateRfc3339TypeConverter::class)
    var created: Date? = null

    @Attribute(converter = DateRfc3339TypeConverter::class)
    var starred: Date? = null

    @Attribute
    var year: Int? = null

    @Attribute
    var genre: String? = null
}