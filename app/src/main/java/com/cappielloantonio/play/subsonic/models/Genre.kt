package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Genre : Parcelable {
    @SerializedName("value")
    var genre: String? = null
    var songCount = 0
    var albumCount = 0
}