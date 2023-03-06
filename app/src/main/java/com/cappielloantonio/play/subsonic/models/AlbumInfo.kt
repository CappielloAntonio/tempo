package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class AlbumInfo {
    @Attribute
    var notes: String? = null

    @Attribute
    var musicBrainzId: String? = null

    @Attribute
    var lastFmUrl: String? = null

    @Attribute
    var smallImageUrl: String? = null

    @Attribute
    var mediumImageUrl: String? = null

    @Attribute
    var largeImageUrl: String? = null
}