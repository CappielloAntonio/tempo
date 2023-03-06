package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.android.parcel.Parcelize

@Parcelize
@Xml
class PlaylistWithSongs : Playlist(), Parcelable {
    @Element(name = "entry")
    var entries: List<Child>? = null
}