package com.cappielloantonio.tempo.subsonic.models

import com.google.gson.annotations.SerializedName

class IndexID3 {
    @SerializedName("artist")
    var artists: List<ArtistID3>? = null
    var name: String? = null
}