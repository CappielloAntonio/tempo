package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class VideoInfo {
    var captions: List<Captions>? = null
    var audioTracks: List<AudioTrack>? = null
    var conversions: List<VideoConversion>? = null
    var id: String? = null
}