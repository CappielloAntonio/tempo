package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.android.parcel.Parcelize

@Parcelize
@Xml(name = "similarArtist")
class SimilarArtistID3 : Parcelable {
    @Attribute
    var id: String? = null

    @Attribute
    var name: String? = null

    @Attribute(name = "coverArt")
    var coverArtId: String? = null

    @Attribute
    var albumCount = 0
}