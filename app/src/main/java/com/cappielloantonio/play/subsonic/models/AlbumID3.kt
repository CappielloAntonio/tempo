package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class AlbumID3 : Parcelable {
    var id: String? = null
    var name: String? = null
    var artist: String? = null
    var artistId: String? = null

    @SerializedName("coverArt")
    var coverArtId: String? = null

    var songCount: Int? = 0
    var duration: Int? = 0
    var playCount: Long? = null
    var created: Date? = null
    var starred: Date? = null
    var year: Int = 0
    var genre: String? = null
}