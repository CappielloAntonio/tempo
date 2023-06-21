package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class ArtistWithAlbumsID3 : ArtistID3(), Parcelable {
    @SerializedName("album")
    var albums: List<AlbumID3>? = null
}