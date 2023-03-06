package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class PlayQueue {
    var entries: List<Child>? = null
    var current: Int? = null
    var position: Long? = null
    var username: String? = null
    var changed: LocalDateTime? = null
    var changedBy: String? = null
}