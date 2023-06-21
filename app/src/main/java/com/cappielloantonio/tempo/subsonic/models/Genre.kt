package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class Genre : Parcelable {
    @SerializedName("value")
    var genre: String? = null
    var songCount = 0
    var albumCount = 0
}