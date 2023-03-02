package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.cappielloantonio.play.subsonic.models.Playlist
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "playlist")
class Playlist(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "playlist_name")
    var name: String,

    @ColumnInfo(name = "primary")
    var primary: String? = null,

    @ColumnInfo(name = "song_count")
    var songCount: Int = 0,

    @ColumnInfo(name = "playlist_duration")
    var duration: Long = 0,

    @ColumnInfo(name = "server")
    var server: String? = null,
) : Parcelable {

    constructor(playlist: Playlist) : this(
        playlist.id,
        playlist.name,
        playlist.coverArtId,
        playlist.songCount,
        playlist.duration.toLong()
    )

    companion object {
        const val ALL = "ALL"
        const val DOWNLOADED = "DOWNLOADED"
        const val ORDER_BY_NAME = "ORDER_BY_NAME"
        const val ORDER_BY_RANDOM = "ORDER_BY_RANDOM"
    }
}