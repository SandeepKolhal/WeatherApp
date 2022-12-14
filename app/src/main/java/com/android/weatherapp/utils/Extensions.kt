package com.android.weatherapp.utils

import android.content.Context
import android.text.format.DateFormat
import android.widget.ImageView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.load
import com.android.weatherapp.models.errors.ResponseError
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun String.toApiErrorResponse(): ResponseError? = try {
    GsonUtils.fromJson(this, ResponseError::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun ImageView.setConditionImage(imageUrl: String) {
    this.load("https:$imageUrl") {
        crossfade(1000)
        build()
    }
}

fun Int.getTime(): String {
    val timestamp = this.toLong() * 1000
    return DateFormat.format("hh:mm a", timestamp).toString()
}

fun String.toDay(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val dt = LocalDate.parse(this, formatter)
    return dt.dayOfWeek.toString()
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCES_NAME)