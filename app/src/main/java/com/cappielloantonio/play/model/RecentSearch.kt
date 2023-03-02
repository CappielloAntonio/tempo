package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "recent_search")
data class RecentSearch(
    @PrimaryKey
    @ColumnInfo(name = "search")
    var search: String
) : Parcelable
