package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "lyrics")
class Lyrics {
    @TextContent
    var content: String? = null

    @Attribute
    var artist: String? = null

    @Attribute
    var title: String? = null
}