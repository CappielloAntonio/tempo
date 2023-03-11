package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PlaylistWithSongs(
    @SerializedName("_id")
    override var id: String
) : Playlist(id), Parcelable {
    @SerializedName("entry")
    var entries: List<Child>? = null
}