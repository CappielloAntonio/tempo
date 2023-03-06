package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class ArtistsID3 {
    @Element(name = "index")
    var indices: List<IndexID3>? = null
    var ignoredArticles: String? = null
}