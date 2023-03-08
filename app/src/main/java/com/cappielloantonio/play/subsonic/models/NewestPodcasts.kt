package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class NewestPodcasts {
    @SerializedName("episode")
    var episodes: List<PodcastEpisode>? = null
}