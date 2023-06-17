package com.cappielloantonio.tempo.subsonic.models

class PodcastStatus {
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