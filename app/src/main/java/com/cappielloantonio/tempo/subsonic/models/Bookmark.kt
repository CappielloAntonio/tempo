package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import java.util.*

@Keep
class Bookmark {
    var entry: Child? = null
    var position: Long = 0
    var username: String? = null
    var comment: String? = null
    var created: Date? = null
    var changed: Date? = null
}