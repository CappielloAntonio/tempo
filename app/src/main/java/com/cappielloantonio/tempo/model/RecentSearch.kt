package com.cappielloantonio.tempo.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "recent_search")
data class RecentSearch(
    @PrimaryKey
    @ColumnInfo(name = "search")
    var search: String
) : Parcelable
