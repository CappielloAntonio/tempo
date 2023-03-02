package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cappielloantonio.play.subsonic.models.Child
import com.cappielloantonio.play.subsonic.models.PodcastEpisode
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class Media(
    val id: String?,
    val title: String?,
    val channelId: String?,
    val streamId: String?,
    val albumId: String?,
    val albumName: String?,
    val artistId: String?,
    val artistName: String?,
    val coverArtId: String?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val year: Int?,
    val duration: Long?,
    val description: String?,
    val status: String?,
    var starred: Boolean?,
    val path: String?,
    val size: Long?,
    val container: String?,
    val bitrate: Int,
    val extension: String?,
    val added: Long?,
    val type: String?,
    val playCount: Int?,
    val lastPlay: Long?,
    val rating: Int?,
    val publishDate: Long?
) : Parcelable {

    constructor(child: Child) : this(
        child.id,
        child.title,
        null,
        null,
        child.albumId,
        child.album,
        child.artistId,
        child.artist,
        child.coverArtId,
        child.track,
        child.discNumber,
        child.year,
        child.duration.toLong(),
        null,
        null,
        child.starred != null,
        child.path,
        child.size,
        child.contentType,
        child.bitRate,
        child.suffix,
        child.created.time,
        child.type,
        0,
        0,
        child.userRating,
        0
    )

    constructor(podcastEpisode: PodcastEpisode) : this(
        podcastEpisode.id,
        podcastEpisode.title,
        podcastEpisode.channelId,
        podcastEpisode.streamId,
        null,
        podcastEpisode.album,
        null,
        podcastEpisode.artist,
        podcastEpisode.coverArtId,
        podcastEpisode.track,
        null,
        podcastEpisode.year,
        podcastEpisode.duration.toLong(),
        podcastEpisode.description,
        podcastEpisode.status,
        podcastEpisode.starred != null,
        null,
        null,
        podcastEpisode.contentType,
        podcastEpisode.bitRate,
        podcastEpisode.suffix,
        podcastEpisode.created.time,
        podcastEpisode.type,
        null,
        null,
        podcastEpisode.userRating,
        podcastEpisode.publishDate.time
    )

    constructor(queue: Queue) : this(
        queue.id,
        queue.title,
        queue.channelId,
        queue.streamId,
        queue.albumId,
        queue.albumName,
        queue.artistId,
        queue.artistName,
        queue.coverArtId,
        null,
        null,
        null,
        queue.duration,
        null,
        null,
        null,
        null,
        null,
        queue.container,
        queue.bitrate,
        queue.extension,
        null,
        queue.type,
        null,
        null,
        null,
        queue.publishingDate
    )

    constructor(download: Download) : this(
        download.mediaID,
        download.title,
        null,
        null,
        download.albumId,
        download.albumName,
        download.artistId,
        download.artistName,
        download.primary,
        download.trackNumber,
        null,
        null,
        download.duration,
        null,
        null,
        null,
        null,
        null,
        download.container,
        download.bitrate,
        download.extension,
        null,
        download.type,
        null,
        null,
        null,
        null
    )

    constructor(item: Chronology) : this(
        item.trackId,
        item.title,
        null,
        null,
        item.albumId,
        item.albumName,
        item.artistId,
        item.artistName,
        item.coverArtId,
        null,
        null,
        null,
        item.duration,
        null,
        null,
        null,
        null,
        null,
        item.container,
        item.bitrate,
        item.extension,
        null,
        MEDIA_TYPE_MUSIC,
        null,
        null,
        null,
        null
    )

    companion object {
        const val MEDIA_TYPE_MUSIC = "music"
        const val MEDIA_TYPE_PODCAST = "podcast"
        const val MEDIA_TYPE_AUDIOBOOK = "audiobook"
        const val MEDIA_TYPE_VIDEO = "video"

        const val MEDIA_PLAYBACK_SPEED_080 = 0.8f
        const val MEDIA_PLAYBACK_SPEED_100 = 1.0f
        const val MEDIA_PLAYBACK_SPEED_125 = 1.25f
        const val MEDIA_PLAYBACK_SPEED_150 = 1.50f
        const val MEDIA_PLAYBACK_SPEED_175 = 1.75f
        const val MEDIA_PLAYBACK_SPEED_200 = 2.0f

        const val RECENTLY_PLAYED = "RECENTLY_PLAYED"
        const val MOST_PLAYED = "MOST_PLAYED"
        const val RECENTLY_ADDED = "RECENTLY_ADDED"
        const val BY_GENRE = "BY_GENRE"
        const val BY_GENRES = "BY_GENRES"
        const val BY_ARTIST = "BY_ARTIST"
        const val BY_YEAR = "BY_YEAR"
        const val STARRED = "STARRED"
        const val DOWNLOADED = "DOWNLOADED"
        const val FROM_ALBUM = "FROM_ALBUM"
    }
}