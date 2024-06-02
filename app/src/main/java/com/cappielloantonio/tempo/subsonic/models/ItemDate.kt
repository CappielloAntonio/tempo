package com.cappielloantonio.tempo.subsonic.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Keep
@Parcelize
open class ItemDate : Parcelable {
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null

    fun getFormattedDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        calendar.set(year ?: 0, month ?: 0, day ?: 0)

        return dateFormat.format(calendar.time)
    }
}