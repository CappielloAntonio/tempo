package com.cappielloantonio.play.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
class Media : Parcelable {
    companion object {
        const val MEDIA_TYPE_MUSIC = "music"
        const val MEDIA_TYPE_PODCAST = "podcast"
        const val MEDIA_TYPE_AUDIOBOOK = "audiobook"
        const val MEDIA_TYPE_VIDEO = "video"

        const val MEDIA_PLAYBACK_SPEED_080 = 0.8f
        const val MEDIA_PLAYBACK_SPEED_100 = 1.0f
        const val MEDIA_PLAYBACK_SPEED_125 = 1.25f
        const val MEDIA_PLAYBACK_SPEED_150 = 1.50f
        const val MEDIA_PLAYBACK_SPEED_175 = 1.75f
        const val MEDIA_PLAYBACK_SPEED_200 = 2.0f

        const val RECENTLY_PLAYED = "RECENTLY_PLAYED"
        const val MOST_PLAYED = "MOST_PLAYED"
        const val RECENTLY_ADDED = "RECENTLY_ADDED"
        const val BY_GENRE = "BY_GENRE"
        const val BY_GENRES = "BY_GENRES"
        const val BY_ARTIST = "BY_ARTIST"
        const val BY_YEAR = "BY_YEAR"
        const val STARRED = "STARRED"
        const val DOWNLOADED = "DOWNLOADED"
        const val FROM_ALBUM = "FROM_ALBUM"
    }
}