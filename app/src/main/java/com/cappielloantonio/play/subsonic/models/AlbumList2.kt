package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class AlbumList2 {
    @Element
    var albums: List<AlbumID3>? = null
}