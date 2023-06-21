package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.Date

@Keep
@Parcelize
class Artist : Parcelable {
    var id: String? = null
    var name: String? = null
    var starred: Date? = null
    var userRating: Int? = null
    var averageRating: Double? = null
}