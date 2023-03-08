package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class ArtistID3 : Parcelable {
    var id: String? = null
    var name: String? = null

    @SerializedName("coverArt")
    var coverArtId: String? = null
    var albumCount = 0
    var starred: Date? = null
}