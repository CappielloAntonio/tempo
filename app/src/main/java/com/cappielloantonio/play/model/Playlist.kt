package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class Playlist : Parcelable {
    companion object {
        const val ALL = "ALL"
        const val DOWNLOADED = "DOWNLOADED"
        const val ORDER_BY_NAME = "ORDER_BY_NAME"
        const val ORDER_BY_RANDOM = "ORDER_BY_RANDOM"
    }
}