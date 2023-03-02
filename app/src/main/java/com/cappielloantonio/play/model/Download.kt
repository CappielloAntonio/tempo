package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.play.App
import com.cappielloantonio.play.util.MusicUtil
import com.cappielloantonio.play.util.PreferenceUtil
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
@Entity(tableName = "download")
class Download(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "mediaId")
    val mediaID: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "albumId")
    val albumId: String,

    @ColumnInfo(name = "albumName")
    val albumName: String,

    @ColumnInfo(name = "artistId")
    val artistId: String,

    @ColumnInfo(name = "artistName")
    val artistName: String,

    @ColumnInfo(name = "trackNumber")
    val trackNumber: Int = 0,

    @ColumnInfo(name = "primary")
    val primary: String,

    @ColumnInfo(name = "duration")
    val duration: Long = 0,

    @ColumnInfo(name = "server")
    val server: String,

    @ColumnInfo(name = "playlistId")
    val playlistId: String? = null,

    @ColumnInfo(name = "playlistName")
    val playlistName: String? = null,

    @ColumnInfo(name = "container")
    val container: String,

    @ColumnInfo(name = "bitrate")
    val bitrate: Int = 0,

    @ColumnInfo(name = "extension")
    val extension: String,

    @ColumnInfo(name = "type")
    val type: String,
) : Parcelable {

    constructor(media: Media, playlistId: String?, playlistName: String?) : this(
        UUID.randomUUID().toString(),
        media.id!!,
        media.title!!,
        media.albumId!!,
        media.albumName!!,
        media.artistId!!,
        MusicUtil.normalizedArtistName(media.artistName),
        media.trackNumber!!,
        media.coverArtId!!,
        media.duration!!,
        PreferenceUtil.getInstance(App.getInstance()).serverId,
        playlistId,
        playlistName,
        media.container!!,
        media.bitrate,
        media.extension!!,
        media.type!!
    )
}