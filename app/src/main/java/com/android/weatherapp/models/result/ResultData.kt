package com.android.weatherapp.models.result

sealed class ResultData<out T> {
    data class Success<out T>(val value: T) : ResultData<T>()
    data class Failure(val error: ErrorData) : ResultData<Nothing>()
    data class Loading(val isLoading: Boolean) : ResultData<Nothing>()
}
