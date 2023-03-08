package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class SearchResult2 {
    @SerializedName("artist")
    var artists: List<Artist>? = null

    @SerializedName("album")
    var albums: List<Child>? = null

    @SerializedName("song")
    var songs: List<Child>? = null
}