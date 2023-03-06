package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Xml(name = "artist")
open class ArtistID3 : Parcelable {
    @Attribute
    var id: String? = null

    @Attribute
    var name: String? = null

    @Attribute(name = "coverArt")
    var coverArtId: String? = null

    @Attribute
    var albumCount = 0

    @Attribute(converter = DateRfc3339TypeConverter::class)
    var starred: Date? = null
}