package com.android.weatherapp.utils

import com.android.weatherapp.models.errors.ResponseError

fun String.toApiErrorResponse(): ResponseError? = try {
    GsonUtils.fromJson(this, ResponseError::class.java)
} catch (e: Exception) {
    e.printStackTrace()
    null
}