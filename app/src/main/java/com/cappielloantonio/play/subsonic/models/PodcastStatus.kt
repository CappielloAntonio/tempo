package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class PodcastStatus {
    @Attribute
    var value: String? = null

    companion object {
        var NEW = "new"
        var DOWNLOADING = "downloading"
        var COMPLETED = "completed"
        var ERROR = "error"
        var DELETED = "deleted"
        var SKIPPED = "skipped"
    }
}