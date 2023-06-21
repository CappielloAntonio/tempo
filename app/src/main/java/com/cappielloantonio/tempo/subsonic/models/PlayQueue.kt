package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.util.*

@Keep
class PlayQueue {
    @SerializedName("entry")
    var entries: List<Child>? = null
    var current: String? = null
    var position: Long? = null
    var username: String? = null
    var changed: Date? = null
    var changedBy: String? = null
}