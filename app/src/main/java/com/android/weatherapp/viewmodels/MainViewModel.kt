package com.android.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.forecast.Forecastday
import com.android.weatherapp.models.result.ErrorData
import com.android.weatherapp.models.result.ResultData
import com.android.weatherapp.repositories.UserPreferenceRepository
import com.android.weatherapp.repositories.WeatherForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

class MainViewModel(
    private val weatherForecastRepository: WeatherForecastRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _weatherForecastDetails = MutableLiveData<ForecastResponse?>()
    val weatherForecastDetails: LiveData<ForecastResponse?>
        get() = _weatherForecastDetails

    private val _forecastApiError = MutableLiveData<ErrorData>()
    val forecastApiError: LiveData<ErrorData>
        get() = _forecastApiError

    private val _forecastDay = MutableLiveData<Forecastday>()
    val forecastDay: LiveData<Forecastday>
        get() = _forecastDay

    private val _tempUnit = MutableLiveData<String?>()
    val tempUnit: LiveData<String?>
        get() = _tempUnit

    private val _windUnit = MutableLiveData<String?>()
    val windUnit: LiveData<String?>
        get() = _windUnit

    private val _lastLocationSearched = MutableLiveData<String?>()
    val lastLocationSearched: LiveData<String?>
        get() = _lastLocationSearched

    private fun fetchSavedUserSelectedTempUnit() {
        viewModelScope.launch {
            userPreferenceRepository.getSavedUserSelectedTempUnit().flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                _tempUnit.postValue(it)
            }
        }
    }

    private fun fetchSavedUserSelectedWindUnit() {
        viewModelScope.launch {
            userPreferenceRepository.getSavedUserSelectedWindUnit().flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                _windUnit.postValue(it)
            }
        }
    }

    private fun fetchSavedUserLastSearchLocation() {
        viewModelScope.launch {
            userPreferenceRepository.getSavedUserLastSearchLocation().flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                _lastLocationSearched.postValue(it)
            }
        }
    }

    fun setSelectedTempUnit(unit: String) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserSelectedTempUnit(unit)
        }
    }

    fun setSelectedWindUnit(unit: String) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserSelectedWindUnit(unit)
        }
    }

    fun setSelectedLocation(location: String) {
        viewModelScope.launch {
            userPreferenceRepository.saveUserLastSearchedLocation(location)
        }
    }

    fun setSelectedForecastDay(forecastDay: Forecastday) {
        _forecastDay.value = forecastDay
    }

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

    fun fetchAllUserPreferenceData() {
        fetchSavedUserLastSearchLocation()
        fetchSavedUserSelectedTempUnit()
        fetchSavedUserSelectedWindUnit()
    }

    fun getLocationList() = mutableListOf(
        "Mumbai",
        "Delhi",
        "Noida",
        "Bangalore",
        "Chennai",
        "Kolkata",
        "New York",
        "Paris",
        "London",
        "Bankok",
        "Tokyo",
        "Dubai",
        "Hong Kong"
    )

}