package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cappielloantonio.play.subsonic.models.Genre
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class Genre(
    val id: String,
    val name: String,
    val songCount: Int = 0,
    val albumCount: Int = 0
) : Parcelable {

    constructor(genre: Genre) : this(
        genre.genre,
        genre.genre,
        genre.songCount,
        genre.albumCount
    )

    companion object {
        const val ORDER_BY_NAME = "ORDER_BY_NAME"
        const val ORDER_BY_RANDOM = "ORDER_BY_RANDOM"
    }
}