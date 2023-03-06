package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class SearchResult2 {
    @Element(name = "artist")
    var artists: List<Artist>? = null

    @Element(name = "album")
    var albums: List<Child>? = null

    @Element(name = "song")
    var songs: List<Child>? = null
}