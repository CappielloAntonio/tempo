package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class ScanStatus {
    @Attribute
    var isScanning = false

    @Attribute
    var count: Long? = null
}