package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class IndexID3 {
    @Element(name = "artist")
    var artists: List<ArtistID3>? = null
    var name: String? = null
}