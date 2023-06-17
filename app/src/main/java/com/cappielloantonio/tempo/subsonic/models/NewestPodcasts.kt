package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class NewestPodcasts {
    @SerializedName("episode")
    var episodes: List<PodcastEpisode>? = null
}