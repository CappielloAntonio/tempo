package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml
import kotlinx.android.parcel.Parcelize

@Parcelize
@Xml
class Genre : Parcelable {
    @TextContent
    var genre: String? = null

    @Attribute
    var songCount = 0

    @Attribute
    var albumCount = 0
}