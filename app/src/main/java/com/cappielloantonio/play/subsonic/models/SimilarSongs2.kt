package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class SimilarSongs2 {
    @SerializedName("song")
    var songs: List<Child>? = null
}