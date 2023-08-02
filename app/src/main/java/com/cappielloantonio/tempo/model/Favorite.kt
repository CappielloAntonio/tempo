package com.cappielloantonio.tempo.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "favorite")
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "timestamp")
    var timestamp: Long,

    @ColumnInfo(name = "songId")
    val songId: String?,

    @ColumnInfo(name = "albumId")
    val albumId: String?,

    @ColumnInfo(name = "artistId")
    val artistId: String?,

    @ColumnInfo(name = "toStar")
    val toStar: Boolean,
) : Parcelable {
    override fun toString(): String = (songId ?: "null") + (albumId ?: "null") + (artistId ?: "null")
}