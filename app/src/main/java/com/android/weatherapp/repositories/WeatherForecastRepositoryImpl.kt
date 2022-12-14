package com.android.weatherapp.repositories

import com.android.weatherapp.datasources.WeatherForecastDataSource
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.result.ResultData
import kotlinx.coroutines.flow.Flow

class WeatherForecastRepositoryImpl(private val weatherForecastDataSource: WeatherForecastDataSource) :
    WeatherForecastRepository {
    override fun fetchWeatherForecast(param: Map<String, String>): Flow<ResultData<ForecastResponse>> {
        return weatherForecastDataSource.fetchWeatherForecast(param)
    }
}