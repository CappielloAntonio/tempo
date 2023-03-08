package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class Playlists(
    @SerializedName("playlist")
    var playlists: List<Playlist>? = null
)