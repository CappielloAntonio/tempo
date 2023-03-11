package com.cappielloantonio.play.subsonic.models

open class JukeboxStatus {
    var currentIndex = 0
    var isPlaying = false
    var gain = 0f
    var position: Int? = null
}