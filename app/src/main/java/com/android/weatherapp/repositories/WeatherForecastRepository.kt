package com.android.weatherapp.repositories

import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.result.ResultData
import kotlinx.coroutines.flow.Flow

interface WeatherForecastRepository {
    fun fetchWeatherForecast(param: Map<String, String>): Flow<ResultData<ForecastResponse>>
}