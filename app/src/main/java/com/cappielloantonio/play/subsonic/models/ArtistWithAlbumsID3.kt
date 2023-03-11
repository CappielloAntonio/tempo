package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ArtistWithAlbumsID3 : ArtistID3(), Parcelable {
    @SerializedName("album")
    var albums: List<AlbumID3>? = null
}