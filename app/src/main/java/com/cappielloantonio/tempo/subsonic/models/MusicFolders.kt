package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class MusicFolders {
    @SerializedName("musicFolder")
    var musicFolders: List<MusicFolder>? = null
}