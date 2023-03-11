package com.cappielloantonio.play.subsonic.models

import java.time.LocalDateTime

class License {
    var isValid = false
    var email: String? = null
    var licenseExpires: LocalDateTime? = null
    var trialExpires: LocalDateTime? = null
}