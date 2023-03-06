package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class ArtistInfo2 : ArtistInfoBase() {
    @Element(name = "similarArtist")
    var similarArtists: List<SimilarArtistID3>? = null
}