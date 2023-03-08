package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class Genres {
    @SerializedName("genre")
    var genres: List<Genre>? = null
}