package com.cappielloantonio.play.subsonic.models

import com.cappielloantonio.play.subsonic.utils.converter.ErrorCodeConverter
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Error {
    @Attribute(converter = ErrorCodeConverter::class)
    var code: ErrorCode? = null

    @Attribute
    var message: String? = null
}