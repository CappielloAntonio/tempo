package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class StructuredLyrics {
    var displayArtist: String? = null
    var displayTitle: String? = null
    var lang: String? = null
    var offset: Int = 0
    var synced: Boolean = false
    var line: List<Line>? = null
}