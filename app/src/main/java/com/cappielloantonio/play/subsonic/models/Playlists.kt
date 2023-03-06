package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Playlists {
    @Element(name = "playlist")
    var playlists: List<Playlist>? = null
}