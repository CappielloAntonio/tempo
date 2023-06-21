package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Indexes {
    var shortcuts: List<Artist>? = null
    @SerializedName("index")
    var indices: List<Index>? = null
    var children: List<Child>? = null
    var lastModified: Long = 0
    var ignoredArticles: String? = null
}