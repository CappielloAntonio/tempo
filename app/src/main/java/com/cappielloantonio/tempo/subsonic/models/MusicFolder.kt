package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MusicFolder : Parcelable {
    var id: String? = null
    var name: String? = null
}