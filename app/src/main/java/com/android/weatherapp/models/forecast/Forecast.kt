package com.android.weatherapp.models.forecast


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday") val forecastDay: List<Forecastday>
)