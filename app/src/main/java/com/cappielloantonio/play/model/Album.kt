package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cappielloantonio.play.subsonic.models.AlbumID3
import com.cappielloantonio.play.subsonic.models.AlbumWithSongsID3
import com.cappielloantonio.play.util.MappingUtil
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
class Album(
    var id: String,
    val title: String,
    val year: Int? = 0,
    val artistId: String,
    val artistName: String,
    val primary: String,
    var starred: Boolean?,
    val songs: List<Media>?,
    val created: Date?
) : Parcelable {

    constructor(albumID3: AlbumID3) : this(
        albumID3.id,
        albumID3.name,
        albumID3.year ?: 0,
        albumID3.artistId,
        albumID3.artist,
        albumID3.coverArtId,
        albumID3.starred != null,
        null,
        albumID3.created
    )

    constructor(albumWithSongsID3: AlbumWithSongsID3) : this(
        albumWithSongsID3.id,
        albumWithSongsID3.name,
        albumWithSongsID3.year ?: 0,
        albumWithSongsID3.artistId,
        albumWithSongsID3.artist,
        albumWithSongsID3.coverArtId,
        albumWithSongsID3.starred != null,
        MappingUtil.mapSong(albumWithSongsID3.songs),
        albumWithSongsID3.created
    )

    constructor(download: Download) : this(
        download.albumId,
        download.albumName,
        0,
        download.artistId,
        download.artistName,
        download.primary,
        null,
        null,
        null
    )

    companion object {
        const val RECENTLY_PLAYED = "RECENTLY_PLAYED"
        const val MOST_PLAYED = "MOST_PLAYED"
        const val RECENTLY_ADDED = "RECENTLY_ADDED"
        const val DOWNLOADED = "DOWNLOADED"
        const val STARRED = "STARRED"
        const val FROM_ARTIST = "FROM_ARTIST"
        const val NEW_RELEASES = "NEW_RELEASES"
        const val ORDER_BY_NAME = "ORDER_BY_NAME"
        const val ORDER_BY_ARTIST = "ORDER_BY_ARTIST"
        const val ORDER_BY_YEAR = "ORDER_BY_YEAR"
        const val ORDER_BY_RANDOM = "ORDER_BY_RANDOM"
    }
}