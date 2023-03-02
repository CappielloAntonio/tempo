package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cappielloantonio.play.subsonic.models.PodcastChannel
import com.cappielloantonio.play.util.MappingUtil
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class PodcastChannel(
    var id: String,
    var url: String,
    var title: String,
    var description: String,
    var coverArtId: String,
    var originalImageUrl: String,
    var status: String,
    var errorMessage: String,
    var episodes: List<Media>,
) : Parcelable {

    constructor(podcastChannel: PodcastChannel) : this(
        podcastChannel.id,
        podcastChannel.url,
        podcastChannel.title,
        podcastChannel.description,
        podcastChannel.coverArtId,
        podcastChannel.originalImageUrl,
        podcastChannel.status,
        podcastChannel.errorMessage,
        MappingUtil.mapPodcastEpisode(podcastChannel.episodes)
    )
}