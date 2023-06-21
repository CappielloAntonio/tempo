package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Starred2 {
    @SerializedName("artist")
    var artists: List<ArtistID3>? = null
    @SerializedName("album")
    var albums: List<AlbumID3>? = null
    @SerializedName("song")
    var songs: List<Child>? = null
}