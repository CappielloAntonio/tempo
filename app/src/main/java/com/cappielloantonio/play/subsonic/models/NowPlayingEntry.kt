package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class NowPlayingEntry(
    @SerializedName("_id")
    override val id: String
) : Child(id) {
    var username: String? = null
    var minutesAgo = 0
    var playerId = 0
    var playerName: String? = null
}