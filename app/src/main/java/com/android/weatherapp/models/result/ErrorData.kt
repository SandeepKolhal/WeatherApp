package com.android.weatherapp.models.result

import com.android.weatherapp.models.errors.ResponseError

data class ErrorData(
    val error: ResponseError? = null,
    val throwable: Throwable? = null,
    val apiErrorCode: Int? = null,
    val apiErrorMessage: String? = null
)
