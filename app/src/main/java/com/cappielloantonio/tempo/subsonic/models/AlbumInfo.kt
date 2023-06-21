package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class AlbumInfo {
    var notes: String? = null
    var musicBrainzId: String? = null
    var lastFmUrl: String? = null
    var smallImageUrl: String? = null
    var mediumImageUrl: String? = null
    var largeImageUrl: String? = null
}