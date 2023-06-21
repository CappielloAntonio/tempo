package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Genres {
    @SerializedName("genre")
    var genres: List<Genre>? = null
}