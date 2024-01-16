package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class PodcastChannel : Parcelable {
    @SerializedName("episode")
    var episodes: List<PodcastEpisode>? = null
    var id: String? = null
    var url: String? = null
    var title: String? = null
    var description: String? = null
    @SerializedName("coverArt")
    var coverArtId: String? = null
    var originalImageUrl: String? = null
    var status: String? = null
    var errorMessage: String? = null
}