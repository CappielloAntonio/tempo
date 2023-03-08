package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName
import java.util.*

class ArtistInfo2 : ArtistInfoBase() {
    @SerializedName("similarArtist")
    var similarArtists: List<SimilarArtistID3>? = emptyList()
}