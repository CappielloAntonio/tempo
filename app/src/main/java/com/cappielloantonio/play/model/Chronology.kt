package com.cappielloantonio.play.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cappielloantonio.play.subsonic.models.Child
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "chronology")
class Chronology(@PrimaryKey override val id: String) : Child(id) {
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis()

    @ColumnInfo(name = "server")
    var server: String? = null
}