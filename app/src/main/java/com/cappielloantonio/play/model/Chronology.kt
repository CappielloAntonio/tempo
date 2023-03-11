package com.cappielloantonio.play.model

import androidx.annotation.Keep
import androidx.media3.common.MediaItem
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.play.subsonic.models.Child
import com.cappielloantonio.play.util.Preferences
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
@Entity(tableName = "chronology")
class Chronology(@PrimaryKey override val id: String) : Child(id) {
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis()

    @ColumnInfo(name = "server")
    var server: String? = null

    constructor(mediaItem: MediaItem) : this(mediaItem.mediaMetadata.extras!!.getString("id")!!) {
        parentId = mediaItem.mediaMetadata.extras!!.getString("parentId")
        isDir = mediaItem.mediaMetadata.extras!!.getBoolean("isDir")
        title = mediaItem.mediaMetadata.extras!!.getString("title")
        album = mediaItem.mediaMetadata.extras!!.getString("album")
        artist = mediaItem.mediaMetadata.extras!!.getString("artist")
        track = mediaItem.mediaMetadata.extras!!.getInt("track")
        year = mediaItem.mediaMetadata.extras!!.getInt("year")
        genre = mediaItem.mediaMetadata.extras!!.getString("genre")
        coverArtId = mediaItem.mediaMetadata.extras!!.getString("coverArtId")
        size = mediaItem.mediaMetadata.extras!!.getLong("size")
        contentType = mediaItem.mediaMetadata.extras!!.getString("contentType")
        suffix = mediaItem.mediaMetadata.extras!!.getString("suffix")
        transcodedContentType = mediaItem.mediaMetadata.extras!!.getString("transcodedContentType")
        transcodedSuffix = mediaItem.mediaMetadata.extras!!.getString("transcodedSuffix")
        duration = mediaItem.mediaMetadata.extras!!.getInt("duration")
        bitrate = mediaItem.mediaMetadata.extras!!.getInt("bitrate")
        path = mediaItem.mediaMetadata.extras!!.getString("path")
        isVideo = mediaItem.mediaMetadata.extras!!.getBoolean("isVideo")
        userRating = mediaItem.mediaMetadata.extras!!.getInt("userRating")
        averageRating = mediaItem.mediaMetadata.extras!!.getDouble("averageRating")
        playCount = mediaItem.mediaMetadata.extras!!.getLong("playCount")
        discNumber = mediaItem.mediaMetadata.extras!!.getInt("discNumber")
        created = Date(mediaItem.mediaMetadata.extras!!.getLong("created"))
        starred = Date(mediaItem.mediaMetadata.extras!!.getLong("starred"))
        albumId = mediaItem.mediaMetadata.extras!!.getString("albumId")
        artistId = mediaItem.mediaMetadata.extras!!.getString("artistId")
        type = mediaItem.mediaMetadata.extras!!.getString("type")
        bookmarkPosition = mediaItem.mediaMetadata.extras!!.getLong("bookmarkPosition")
        originalWidth = mediaItem.mediaMetadata.extras!!.getInt("originalWidth")
        originalHeight = mediaItem.mediaMetadata.extras!!.getInt("originalHeight")
        server = Preferences.getServerId()
        timestamp = Date().time
    }
}