package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class AlbumList2 {
    @SerializedName("album")
    var albums: List<AlbumID3>? = null
}