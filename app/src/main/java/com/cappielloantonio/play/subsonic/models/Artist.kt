package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
class Artist : Parcelable {
    var id: String? = null
    var name: String? = null
    var starred: Date? = null
    var userRating: Int? = null
    var averageRating: Double? = null
}