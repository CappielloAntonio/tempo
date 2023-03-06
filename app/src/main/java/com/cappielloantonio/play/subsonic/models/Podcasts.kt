package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Podcasts {
    @Element(name = "channel")
    var channels: List<PodcastChannel>? = null
}