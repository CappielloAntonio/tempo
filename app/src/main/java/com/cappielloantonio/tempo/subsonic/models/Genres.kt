package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class Genres {
    @SerializedName("genre")
    var genres: List<Genre>? = null
}