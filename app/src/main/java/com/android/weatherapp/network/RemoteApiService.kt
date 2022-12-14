package com.android.weatherapp.network

import com.android.weatherapp.models.forecast.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RemoteApiService {
    @GET("forecast.json")
    suspend fun fetchWeatherForecast(@QueryMap param: Map<String, String>): Response<ForecastResponse>
}