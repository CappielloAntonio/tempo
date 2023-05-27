package com.cappielloantonio.play.subsonic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
class Directory : Parcelable {
    @SerializedName("child")
    var children: List<Child>? = null
    var id: String? = null
    @SerializedName("parent")
    var parentId: String? = null
    var name: String? = null
    var starred: Date? = null
    var userRating: Int? = null
    var averageRating: Double? = null
    var playCount: Long? = null
}