package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class PodcastChannel : Parcelable {
    @SerializedName("episode")
    var episodes: List<PodcastEpisode>? = null
    var id: String? = null
    var url: String? = null
    var title: String? = null
    var description: String? = null
    var coverArtId: String? = null
    var originalImageUrl: String? = null
    var status: String? = null
    var errorMessage: String? = null
}