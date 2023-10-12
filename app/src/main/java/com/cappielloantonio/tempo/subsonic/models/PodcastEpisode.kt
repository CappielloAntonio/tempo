package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
class PodcastEpisode : Parcelable {
    var id: String? = null
    @SerializedName("parent")
    var parentId: String? = null
    var isDir = false
    var title: String? = null
    var album: String? = null
    var artist: String? = null
    var year: Int? = null
    var genre: String? = null
    @SerializedName("coverArt")
    var coverArtId: String? = null
    var size: Long? = null
    var contentType: String? = null
    var suffix: String? = null
    var duration: Int? = null
    @SerializedName("bitRate")
    var bitrate: Int? = null
    var path: String? = null
    var isVideo: Boolean = false
    var created: Date? = null
    var artistId: String? = null
    var type: String? = null
    var streamId: String? = null
    var channelId: String? = null
    var description: String? = null
    var status: String? = null
    var publishDate: Date? = null
}