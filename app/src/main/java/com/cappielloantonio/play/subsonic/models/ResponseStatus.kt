package com.cappielloantonio.play.subsonic.models

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class ResponseStatus(@param:Attribute val value: String) {

    companion object {
        @JvmField
        var OK = "ok"

        @JvmField
        var FAILED = "failed"
    }
}