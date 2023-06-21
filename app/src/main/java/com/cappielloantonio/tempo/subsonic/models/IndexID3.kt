package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class IndexID3 {
    @SerializedName("artist")
    var artists: List<ArtistID3>? = null
    var name: String? = null
}