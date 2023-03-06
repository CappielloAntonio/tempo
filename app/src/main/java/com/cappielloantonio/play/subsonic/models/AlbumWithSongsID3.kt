package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.android.parcel.Parcelize

@Parcelize
@Xml
class AlbumWithSongsID3 : AlbumID3(), Parcelable {
    @Element(name = "song")
    var songs: List<Child>? = null
}