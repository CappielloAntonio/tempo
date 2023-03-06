package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Share {
    var entries: List<Child>? = null
    var id: String? = null
    var url: String? = null
    var description: String? = null
    var username: String? = null
    var created: LocalDateTime? = null
    var expires: LocalDateTime? = null
    var lastVisited: LocalDateTime? = null
    var visitCount = 0
}