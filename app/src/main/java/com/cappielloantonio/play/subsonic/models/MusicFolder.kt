package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class MusicFolder {
    @Attribute
    var id = 0

    @Attribute
    var name: String? = null
}