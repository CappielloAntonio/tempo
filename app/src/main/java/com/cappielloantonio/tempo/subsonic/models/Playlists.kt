package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Playlists(
    @SerializedName("playlist")
    var playlists: List<Playlist>? = null
)