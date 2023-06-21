package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class SimilarSongs2 {
    @SerializedName("song")
    var songs: List<Child>? = null
}