package com.cappielloantonio.play.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.play.subsonic.models.Child
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "queue")
class Queue(override val id: String) : Child(id) {
    @PrimaryKey
    @ColumnInfo(name = "track_order")
    var trackOrder: Int = 0

    @ColumnInfo(name = "last_play")
    var lastPlay: Long = 0

    @ColumnInfo(name = "playing_changed")
    var playingChanged: Long = 0

    @ColumnInfo(name = "stream_id")
    var streamId: String? = null

    constructor(child: Child) : this(child.id) {
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
        type = child.type
        bookmarkPosition = child.bookmarkPosition
        originalWidth = child.originalWidth
        originalHeight = child.originalHeight
    }
}