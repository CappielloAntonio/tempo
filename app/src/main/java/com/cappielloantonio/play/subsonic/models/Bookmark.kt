package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Bookmark {
    var entry: Child? = null
    var position: Long = 0
    var username: String? = null
    var comment: String? = null
    var created: LocalDateTime? = null
    var changed: LocalDateTime? = null
}