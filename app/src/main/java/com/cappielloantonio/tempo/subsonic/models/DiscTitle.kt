package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
open class DiscTitle : Parcelable {
    var disc: Int? = null
    var title: String? = null
}