package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class SearchResult3 {
    @Element(name = "artist")
    var artists: List<ArtistID3>? = null

    @Element(name = "album")
    var albums: List<AlbumID3>? = null

    @Element(name = "song")
    var songs: List<Child>? = null
}