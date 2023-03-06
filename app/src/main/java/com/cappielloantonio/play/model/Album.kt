package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class Album : Parcelable {
    companion object {
        const val RECENTLY_PLAYED = "RECENTLY_PLAYED"
        const val MOST_PLAYED = "MOST_PLAYED"
        const val RECENTLY_ADDED = "RECENTLY_ADDED"
        const val DOWNLOADED = "DOWNLOADED"
        const val STARRED = "STARRED"
        const val FROM_ARTIST = "FROM_ARTIST"
        const val NEW_RELEASES = "NEW_RELEASES"
        const val ORDER_BY_NAME = "ORDER_BY_NAME"
        const val ORDER_BY_ARTIST = "ORDER_BY_ARTIST"
        const val ORDER_BY_YEAR = "ORDER_BY_YEAR"
        const val ORDER_BY_RANDOM = "ORDER_BY_RANDOM"
    }
}