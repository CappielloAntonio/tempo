package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
open class ItemDate : Parcelable {
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null
}