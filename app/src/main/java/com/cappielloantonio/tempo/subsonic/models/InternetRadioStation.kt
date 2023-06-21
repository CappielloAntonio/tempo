package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class InternetRadioStation : Parcelable {
    var id: String? = null
    var name: String? = null
    var streamUrl: String? = null
    var homePageUrl: String? = null
}