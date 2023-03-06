package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class TopSongs {
    @Element(name = "song")
    var songs: List<Child>? = null
}