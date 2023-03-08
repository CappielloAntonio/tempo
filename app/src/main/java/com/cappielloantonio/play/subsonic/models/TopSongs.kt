package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class TopSongs {
    @SerializedName("song")
    var songs: List<Child>? = null
}