package com.cappielloantonio.play.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.play.subsonic.models.Child
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "queue")
class Queue(override val id: String) : Child(id) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "track_order")
    var trackOrder: Int = 0

    @ColumnInfo(name = "last_play", defaultValue = "0")
    var lastPlay: Long = 0

    @ColumnInfo(name = "playing_changed", defaultValue = "0")
    var playingChanged: Long = 0

    @ColumnInfo(name = "stream_id")
    var streamId: String? = null
}