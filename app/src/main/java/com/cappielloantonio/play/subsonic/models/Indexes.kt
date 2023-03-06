package com.cappielloantonio.play.subsonic.models

class Indexes {
    var shortcuts: List<Artist>? = null
    var indices: List<Index>? = null
    var children: List<Child>? = null
    var lastModified: Long = 0
    var ignoredArticles: String? = null
}