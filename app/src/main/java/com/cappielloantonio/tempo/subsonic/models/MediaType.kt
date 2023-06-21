package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class MediaType {
    var value: String? = null

    companion object {
        var MUSIC = "music"
        var PODCAST = "podcast"
        var AUDIOBOOK = "audiobook"
        var VIDEO = "video"
    }
}