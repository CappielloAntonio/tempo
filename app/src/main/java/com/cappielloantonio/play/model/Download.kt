package com.cappielloantonio.play.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.play.subsonic.models.Child
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "download")
class Download(@PrimaryKey override val id: String) : Child(id) {
    @ColumnInfo(name = "media_id")
    var mediaID: String? = null

    @ColumnInfo
    var server: String? = null

    @ColumnInfo(name = "playlist_id")
    var playlistId: String? = null
}