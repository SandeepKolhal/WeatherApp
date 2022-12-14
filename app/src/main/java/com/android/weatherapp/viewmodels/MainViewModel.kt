package com.android.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.result.ErrorData
import com.android.weatherapp.models.result.ResultData
import com.android.weatherapp.repositories.WeatherForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

class MainViewModel(private val weatherForecastRepository: WeatherForecastRepository) :
    ViewModel() {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _weatherForecastDetails = MutableLiveData<ForecastResponse?>()
    val weatherForecastDetails: LiveData<ForecastResponse?>
        get() = _weatherForecastDetails

    private val _forecastApiError = MutableLiveData<ErrorData>()
    val forecastApiError: LiveData<ErrorData>
        get() = _forecastApiError


    fun fetchForecastData(param: Map<String, String>) {
        viewModelScope.launch {
            weatherForecastRepository.fetchWeatherForecast(param).flowOn(Dispatchers.IO)
                .retry(retries = 3) {
                    _forecastApiError.postValue(ErrorData(throwable = it))
                    it.printStackTrace()
                    return@retry true
                }.catch {
                    _forecastApiError.postValue(ErrorData(throwable = it))
                    it.printStackTrace()
                }.collect { resultData ->
                    when (resultData) {
                        is ResultData.Failure -> {
                            _forecastApiError.postValue(resultData.error)
                        }
                        is ResultData.Loading -> {
                            _isLoading.postValue(resultData.isLoading)
                        }
                        is ResultData.Success -> {
                            _weatherForecastDetails.postValue(resultData.value)
                        }
                    }
                }
        }
    }

}