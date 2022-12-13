package com.android.weatherapp.models.errors


import com.google.gson.annotations.SerializedName

data class ResponseError(
    @SerializedName("error")
    val error: Error
)