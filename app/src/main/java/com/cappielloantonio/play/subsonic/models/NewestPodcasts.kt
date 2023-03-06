package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class NewestPodcasts {
    @Element(name = "episode")
    var episodes: List<PodcastEpisode>? = null
}