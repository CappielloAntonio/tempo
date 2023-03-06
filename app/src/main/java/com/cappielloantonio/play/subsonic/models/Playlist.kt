package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "playlist")
@Xml
open class Playlist : Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @Attribute
    lateinit var id: String

    @ColumnInfo(name = "name")
    @Attribute
    var name: String? = null

    @Ignore
    @Attribute
    var comment: String? = null

    @Ignore
    @Attribute
    var owner: String? = null

    @Ignore
    @Attribute(name = "public")
    var isUniversal: Boolean? = null

    @Ignore
    @Attribute
    var songCount: Int = 0

    @Ignore
    @ColumnInfo(name = "duration")
    @Attribute
    var duration: Long = 0

    @Ignore
    @Attribute(converter = DateRfc3339TypeConverter::class)
    var created: Date? = null

    @Ignore
    @Attribute(converter = DateRfc3339TypeConverter::class)
    var changed: Date? = null

    @Ignore
    @ColumnInfo(name = "coverArt")
    @Attribute
    var coverArtId: String? = null

    @Ignore
    @Attribute
    var allowedUsers: List<String>? = null
}