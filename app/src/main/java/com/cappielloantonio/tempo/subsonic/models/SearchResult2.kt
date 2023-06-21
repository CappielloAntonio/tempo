package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class SearchResult2 {
    @SerializedName("artist")
    var artists: List<Artist>? = null
    @SerializedName("album")
    var albums: List<Child>? = null
    @SerializedName("song")
    var songs: List<Child>? = null
}