package com.cappielloantonio.tempo.subsonic.base

import com.cappielloantonio.tempo.subsonic.models.SubsonicResponse
import com.google.gson.annotations.SerializedName

class ApiResponse {
    @SerializedName("subsonic-response")
    var subsonicResponse: SubsonicResponse? = null
}