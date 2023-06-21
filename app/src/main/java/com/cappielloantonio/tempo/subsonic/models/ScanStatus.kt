package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep

@Keep
class ScanStatus {
    var isScanning = false
    var count: Long? = null
}