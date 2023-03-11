package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class Directory {
    var children: List<Child>? = null
    var id: String? = null
    var parentId: String? = null
    var name: String? = null
    var starred: LocalDateTime? = null
    var userRating: Int? = null
    var averageRating: Double? = null
    var playCount: Long? = null
}