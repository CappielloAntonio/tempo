package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
class MusicFolders {
    @Element
    var musicFolders: List<MusicFolder>? = null
}