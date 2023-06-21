package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Podcasts {
    @SerializedName("channel")
    var channels: List<PodcastChannel>? = null
}