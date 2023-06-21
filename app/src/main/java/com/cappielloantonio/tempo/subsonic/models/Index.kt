package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Index {
    @SerializedName("artist")
    var artists: List<Artist>? = null
    var name: String? = null
}