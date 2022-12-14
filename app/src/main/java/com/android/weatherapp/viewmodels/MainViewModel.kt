package com.android.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.result.ResultData
import com.android.weatherapp.repositories.WeatherForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MainViewModel(private val weatherForecastRepository: WeatherForecastRepository) :
    ViewModel() {

    var resultData: Flow<ResultData<ForecastResponse>> = MutableSharedFlow()

    fun fetchForecastData(param: Map<String, String>) {
        viewModelScope.launch {
            resultData =
                weatherForecastRepository.fetchWeatherForecast(param).flowOn(Dispatchers.IO).catch {

                }
        }
    }

}