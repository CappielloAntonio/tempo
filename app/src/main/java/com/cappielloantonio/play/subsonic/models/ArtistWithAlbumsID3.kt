package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.android.parcel.Parcelize

@Parcelize
@Xml
class ArtistWithAlbumsID3 : ArtistID3(), Parcelable {
    @Element(name = "album")
    var albums: List<AlbumID3>? = null
}