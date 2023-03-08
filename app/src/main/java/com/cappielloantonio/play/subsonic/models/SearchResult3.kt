package com.cappielloantonio.play.subsonic.models

import com.google.gson.annotations.SerializedName

class SearchResult3 {
    @SerializedName("artist")
    var artists: List<ArtistID3>? = null

    @SerializedName("album")
    var albums: List<AlbumID3>? = null

    @SerializedName("song")
    var songs: List<Child>? = null
}