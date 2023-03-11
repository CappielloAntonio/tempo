package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Artist {
    var id: String? = null
    var name: String? = null
    var starred: LocalDateTime? = null
    var userRating: Int? = null
    var averageRating: Double? = null
}