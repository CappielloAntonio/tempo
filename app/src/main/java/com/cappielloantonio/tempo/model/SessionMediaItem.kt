package com.cappielloantonio.tempo.model

import android.net.Uri
import android.os.Bundle
import androidx.annotation.Keep
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.RequestMetadata
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.tempo.glide.CustomGlideRequest
import com.cappielloantonio.tempo.subsonic.models.Child
import com.cappielloantonio.tempo.subsonic.models.InternetRadioStation
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode
import com.cappielloantonio.tempo.util.Constants
import com.cappielloantonio.tempo.util.MusicUtil
import com.cappielloantonio.tempo.util.Preferences.getImageSize
import java.util.Date

@Keep
@Entity(tableName = "session_media_item")
class SessionMediaItem() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    var index: Int = 0

    @ColumnInfo(name = "id")
    var id: String? = null

    @ColumnInfo(name = "parent_id")
    var parentId: String? = null

    @ColumnInfo(name = "is_dir")
    var isDir: Boolean = false

    @ColumnInfo
    var title: String? = null

    @ColumnInfo
    var album: String? = null

    @ColumnInfo
    var artist: String? = null

    @ColumnInfo
    var track: Int? = null

    @ColumnInfo
    var year: Int? = null

    @ColumnInfo
    var genre: String? = null

    @ColumnInfo(name = "cover_art_id")
    var coverArtId: String? = null

    @ColumnInfo
    var size: Long? = null

    @ColumnInfo(name = "content_type")
    var contentType: String? = null

    @ColumnInfo
    var suffix: String? = null

    @ColumnInfo("transcoding_content_type")
    var transcodedContentType: String? = null

    @ColumnInfo(name = "transcoded_suffix")
    var transcodedSuffix: String? = null

    @ColumnInfo
    var duration: Int? = null

    @ColumnInfo("bitrate")
    var bitrate: Int? = null

    @ColumnInfo
    var path: String? = null

    @ColumnInfo(name = "is_video")
    var isVideo: Boolean = false

    @ColumnInfo(name = "user_rating")
    var userRating: Int? = null

    @ColumnInfo(name = "average_rating")
    var averageRating: Double? = null

    @ColumnInfo(name = "play_count")
    var playCount: Long? = null

    @ColumnInfo(name = "disc_number")
    var discNumber: Int? = null

    @ColumnInfo
    var created: Date? = null

    @ColumnInfo
    var starred: Date? = null

    @ColumnInfo(name = "album_id")
    var albumId: String? = null

    @ColumnInfo(name = "artist_id")
    var artistId: String? = null

    @ColumnInfo
    var type: String? = null

    @ColumnInfo(name = "bookmark_position")
    var bookmarkPosition: Long? = null

    @ColumnInfo(name = "original_width")
    var originalWidth: Int? = null

    @ColumnInfo(name = "original_height")
    var originalHeight: Int? = null

    @ColumnInfo(name = "stream_id")
    var streamId: String? = null

    @ColumnInfo(name = "stream_url")
    var streamUrl: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long? = null

    constructor(child: Child) : this() {
        id = child.id
        parentId = child.parentId
        isDir = child.isDir
        title = child.title
        album = child.album
        artist = child.artist
        track = child.track
        year = child.year
        genre = child.genre
        coverArtId = child.coverArtId
        size = child.size
        contentType = child.contentType
        suffix = child.suffix
        transcodedContentType = child.transcodedContentType
        transcodedSuffix = child.transcodedSuffix
        duration = child.duration
        bitrate = child.bitrate
        path = child.path
        isVideo = child.isVideo
        userRating = child.userRating
        averageRating = child.averageRating
        playCount = child.playCount
        discNumber = child.discNumber
        created = child.created
        starred = child.starred
        albumId = child.albumId
        artistId = child.artistId
        type = Constants.MEDIA_TYPE_MUSIC
        bookmarkPosition = child.bookmarkPosition
        originalWidth = child.originalWidth
        originalHeight = child.originalHeight
    }

    constructor(podcastEpisode: PodcastEpisode) : this() {
        id = podcastEpisode.id
        parentId = podcastEpisode.parentId
        isDir = podcastEpisode.isDir
        title = podcastEpisode.title
        album = podcastEpisode.album
        artist = podcastEpisode.artist
        year = podcastEpisode.year
        genre = podcastEpisode.genre
        coverArtId = podcastEpisode.coverArtId
        size = podcastEpisode.size
        contentType = podcastEpisode.contentType
        suffix = podcastEpisode.suffix
        duration = podcastEpisode.duration
        bitrate = podcastEpisode.bitrate
        path = podcastEpisode.path
        isVideo = podcastEpisode.isVideo
        created = podcastEpisode.created
        artistId = podcastEpisode.artistId
        streamId = podcastEpisode.streamId
        type = Constants.MEDIA_TYPE_PODCAST
    }

    constructor(internetRadioStation: InternetRadioStation) : this() {
        id = internetRadioStation.id
        title = internetRadioStation.name
        streamUrl = internetRadioStation.streamUrl
        type = Constants.MEDIA_TYPE_RADIO
    }

    fun getMediaItem(): MediaItem {
        val uri: Uri = getStreamUri()
        val artworkUri = Uri.parse(CustomGlideRequest.createUrl(coverArtId, getImageSize()))

        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("parentId", parentId)
        bundle.putBoolean("isDir", isDir)
        bundle.putString("title", title)
        bundle.putString("album", album)
        bundle.putString("artist", artist)
        bundle.putInt("track", track ?: 0)
        bundle.putInt("year", year ?: 0)
        bundle.putString("genre", genre)
        bundle.putString("coverArtId", coverArtId)
        bundle.putLong("size", size ?: 0)
        bundle.putString("contentType", contentType)
        bundle.putString("suffix", suffix)
        bundle.putString("transcodedContentType", transcodedContentType)
        bundle.putString("transcodedSuffix", transcodedSuffix)
        bundle.putInt("duration", duration ?: 0)
        bundle.putInt("bitrate", bitrate ?: 0)
        bundle.putString("path", path)
        bundle.putBoolean("isVideo", isVideo)
        bundle.putInt("userRating", userRating ?: 0)
        bundle.putDouble("averageRating", averageRating ?: .0)
        bundle.putLong("playCount", playCount ?: 0)
        bundle.putInt("discNumber", discNumber ?: 0)
        bundle.putLong("created", created?.time ?: 0)
        bundle.putLong("starred", starred?.time ?: 0)
        bundle.putString("albumId", albumId)
        bundle.putString("artistId", artistId)
        bundle.putString("type", Constants.MEDIA_TYPE_MUSIC)
        bundle.putLong("bookmarkPosition", bookmarkPosition ?: 0)
        bundle.putInt("originalWidth", originalWidth ?: 0)
        bundle.putInt("originalHeight", originalHeight ?: 0)
        bundle.putString("uri", uri.toString())

        return MediaItem.Builder()
            .setMediaId(id!!)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(MusicUtil.getReadableString(title))
                    .setTrackNumber(track ?: 0)
                    .setDiscNumber(discNumber ?: 0)
                    .setReleaseYear(year ?: 0)
                    .setAlbumTitle(MusicUtil.getReadableString(album))
                    .setArtist(MusicUtil.getReadableString(artist))
                    .setArtworkUri(artworkUri)
                    .setExtras(bundle)
                    .setIsBrowsable(false)
                    .setIsPlayable(true)
                    .build()
            )
            .setRequestMetadata(
                RequestMetadata.Builder()
                    .setMediaUri(uri)
                    .setExtras(bundle)
                    .build()
            )
            .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
            .setUri(uri)
            .build()
    }

    private fun getStreamUri(): Uri {
        return when (type) {
            Constants.MEDIA_TYPE_MUSIC -> {
                MusicUtil.getStreamUri(id)
            }

            Constants.MEDIA_TYPE_PODCAST -> {
                MusicUtil.getStreamUri(streamId)
            }

            Constants.MEDIA_TYPE_RADIO -> {
                Uri.parse(streamUrl)
            }

            else -> {
                MusicUtil.getStreamUri(id)
            }
        }
    }
}