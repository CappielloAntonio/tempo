package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class ArtistsID3 {
    @SerializedName("index")
    var indices: List<IndexID3>? = null
    var ignoredArticles: String? = null
}