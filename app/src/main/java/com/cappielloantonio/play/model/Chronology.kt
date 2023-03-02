package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "chronology")
data class Chronology(
    @ColumnInfo(name = "id")
    val trackId: String,

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

    @ColumnInfo(name = "cover_art_id")
    val coverArtId: String,

    @ColumnInfo(name = "duration")
    val duration: Long,

    @ColumnInfo(name = "container")
    val container: String,

    @ColumnInfo(name = "bitrate")
    val bitrate: Int,

    @ColumnInfo(name = "extension")
    val extension: String,

    @ColumnInfo(name = "server")
    val server: String,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis()
}