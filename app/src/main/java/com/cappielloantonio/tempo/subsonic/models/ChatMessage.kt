package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class ChatMessage {
    var username: String? = null
    var time: Long = 0
    var message: String? = null
}