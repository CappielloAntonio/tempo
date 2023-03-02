package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "queue")
data class Queue(
    @PrimaryKey
    @ColumnInfo(name = "track_order")
    val trackOrder: Int,

    @ColumnInfo(name = "id")
    val id: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "albumId")
    val albumId: String?,

    @ColumnInfo(name = "albumName")
    val albumName: String?,

    @ColumnInfo(name = "artistId")
    val artistId: String?,

    @ColumnInfo(name = "artistName")
    val artistName: String?,

    @ColumnInfo(name = "cover_art_id")
    val coverArtId: String?,

    @ColumnInfo(name = "duration")
    val duration: Long,

    @ColumnInfo(name = "last_play", defaultValue = "0")
    val lastPlay: Long,

    @ColumnInfo(name = "playing_changed", defaultValue = "0")
    val playingChanged: Long,

    @ColumnInfo(name = "stream_id")
    val streamId: String?,

    @ColumnInfo(name = "channel_id")
    val channelId: String?,

    @ColumnInfo(name = "publishing_date", defaultValue = "0")
    val publishingDate: Long,

    @ColumnInfo(name = "container")
    val container: String?,

    @ColumnInfo(name = "bitrate")
    val bitrate: Int,

    @ColumnInfo(name = "extension")
    val extension: String?,

    @ColumnInfo(name = "media_type")
    val type: String?
) : Parcelable