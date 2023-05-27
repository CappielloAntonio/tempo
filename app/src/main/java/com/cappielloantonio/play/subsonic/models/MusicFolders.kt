package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class MusicFolders {
    @SerializedName("musicFolder")
    var musicFolders: List<MusicFolder>? = null
}