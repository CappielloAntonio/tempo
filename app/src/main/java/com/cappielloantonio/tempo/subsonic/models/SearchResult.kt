package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class SearchResult {
    var matches: List<Child>? = null
    var offset = 0
    var totalHits = 0
}