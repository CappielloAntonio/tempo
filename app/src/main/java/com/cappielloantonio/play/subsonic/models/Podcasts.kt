package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class Podcasts {
    @SerializedName("channel")
    var channels: List<PodcastChannel>? = null
}