package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class Error {
    var code: ErrorCode? = null
    var message: String? = null
}