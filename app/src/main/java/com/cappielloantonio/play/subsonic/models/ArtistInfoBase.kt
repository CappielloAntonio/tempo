package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
open class ArtistInfoBase {
    @PropertyElement
    var biography: String? = null

    @PropertyElement
    var musicBrainzId: String? = null

    @PropertyElement
    var lastFmUrl: String? = null

    @PropertyElement
    var smallImageUrl: String? = null

    @PropertyElement
    var mediumImageUrl: String? = null

    @PropertyElement
    var largeImageUrl: String? = null
}