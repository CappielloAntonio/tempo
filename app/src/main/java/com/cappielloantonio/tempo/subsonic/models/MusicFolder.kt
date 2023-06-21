package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class MusicFolder : Parcelable {
    var id: String? = null
    var name: String? = null
}