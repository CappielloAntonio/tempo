package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class MediaType {
    @Attribute
    var value: String? = null

    companion object {
        var MUSIC = "music"
        var PODCAST = "podcast"
        var AUDIOBOOK = "audiobook"
        var VIDEO = "video"
    }
}