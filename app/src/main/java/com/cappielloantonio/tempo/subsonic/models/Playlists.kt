package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class Playlists(
    @SerializedName("playlist")
    var playlists: List<Playlist>? = null
)