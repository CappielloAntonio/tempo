package com.cappielloantonio.tempo.subsonic.base

import androidx.annotation.Keep
import com.cappielloantonio.tempo.subsonic.models.SubsonicResponse
import com.google.gson.annotations.SerializedName

@Keep
class ApiResponse {
    @SerializedName("subsonic-response")
    var subsonicResponse: SubsonicResponse? = null
}