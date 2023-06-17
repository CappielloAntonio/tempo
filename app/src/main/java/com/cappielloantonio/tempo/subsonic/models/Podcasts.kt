package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class Podcasts {
    @SerializedName("channel")
    var channels: List<PodcastChannel>? = null
}