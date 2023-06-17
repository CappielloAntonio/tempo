package com.cappielloantonio.tempo.subsonic.models

import java.util.*

class Share {
    var entries: List<Child>? = null
    var id: String? = null
    var url: String? = null
    var description: String? = null
    var username: String? = null
    var created: Date? = null
    var expires: Date? = null
    var lastVisited: Date? = null
    var visitCount = 0
}