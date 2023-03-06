package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.android.parcel.Parcelize

@Parcelize
@Xml
class PodcastChannel : Parcelable {
    @Element(name = "episode")
    var episodes: List<PodcastEpisode>? = null

    @Attribute
    var id: String? = null

    @Attribute
    var url: String? = null

    @Attribute
    var title: String? = null

    @Attribute
    var description: String? = null

    @Attribute
    var coverArtId: String? = null

    @Attribute
    var originalImageUrl: String? = null

    @Attribute
    var status: String? = null

    @Attribute
    var errorMessage: String? = null
}