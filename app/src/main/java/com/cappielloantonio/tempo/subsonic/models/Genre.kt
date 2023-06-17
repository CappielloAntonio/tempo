package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Genre : Parcelable {
    @SerializedName("value")
    var genre: String? = null
    var songCount = 0
    var albumCount = 0
}