package com.android.weatherapp.datasources

import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.result.ResultData
import kotlinx.coroutines.flow.Flow

interface WeatherForecastDataSource {
    fun fetchWeatherForecast(param: Map<String, String>): Flow<ResultData<ForecastResponse>>
}