package com.cappielloantonio.tempo.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MapTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToMap(value: String?): Map<String, String> {
        return if (value.isNullOrEmpty()) {
            emptyMap()
        } else {
            try {
                Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)
                    ?: emptyMap()
            } catch (e: Exception) {
                emptyMap()
            }
        }
    }


    @TypeConverter
    @JvmStatic
    fun mapToString(value: Map<String, String>?): String {
        return value?.let { Gson().toJson(it) } ?: ""
    }
}

