package com.cappielloantonio.tempo.subsonic.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class InternetRadioStations {
    @SerializedName("internetRadioStation")
    var internetRadioStations: List<InternetRadioStation>? = null
}