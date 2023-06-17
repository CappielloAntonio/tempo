package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class Songs {
    @SerializedName("song")
    var songs: List<Child>? = null
}