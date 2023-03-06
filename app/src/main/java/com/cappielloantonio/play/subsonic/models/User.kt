package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class User {
    var folders: List<Int>? = null
    var username: String? = null
    var email: String? = null
    var isScrobblingEnabled = false
    var maxBitRate: Int? = null
    var isAdminRole = false
    var isSettingsRole = false
    var isDownloadRole = false
    var isUploadRole = false
    var isPlaylistRole = false
    var isCoverArtRole = false
    var isCommentRole = false
    var isPodcastRole = false
    var isStreamRole = false
    var isJukeboxRole = false
    var isShareRole = false
    var isVideoConversionRole = false
    var avatarLastChanged: LocalDateTime? = null
}