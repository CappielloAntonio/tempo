package com.cappielloantonio.play.subsonic.base

import com.cappielloantonio.play.subsonic.models.SubsonicResponse
import com.google.gson.annotations.SerializedName

class ApiResponse {
    @SerializedName("subsonic-response")
    var subsonicResponse: SubsonicResponse? = null
}