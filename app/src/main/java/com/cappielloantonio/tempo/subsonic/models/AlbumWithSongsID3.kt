package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class AlbumWithSongsID3 : AlbumID3(), Parcelable {
    @SerializedName("song")
    var songs: List<Child>? = null
}