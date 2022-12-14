package com.android.weatherapp.utils

import android.widget.ImageView
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

fun String.getTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val dt = LocalDate.parse(this, formatter)
    return dt.toString()
}

fun String.toDay(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val dt = LocalDate.parse(this, formatter)
    return dt.dayOfWeek.toString()
}