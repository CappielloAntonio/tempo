package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class AlbumList2 {
    @SerializedName("album")
    var albums: List<AlbumID3>? = null
}