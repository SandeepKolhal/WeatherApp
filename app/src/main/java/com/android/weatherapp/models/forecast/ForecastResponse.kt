package com.android.weatherapp.models.forecast


import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("current") val current: Current,
    @SerializedName("forecast") val forecast: Forecast,
    @SerializedName("location") val location: Location
)