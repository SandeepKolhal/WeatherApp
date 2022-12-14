package com.android.weatherapp.datasources

import android.util.Log
import com.android.weatherapp.models.forecast.ForecastResponse
import com.android.weatherapp.models.result.ErrorData
import com.android.weatherapp.models.result.ResultData
import com.android.weatherapp.network.RemoteApiService
import com.android.weatherapp.utils.toApiErrorResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class RemoteApiDataSourceImpl(private val remoteApiService: RemoteApiService) :
    RemoteApiDataSource {

    companion object {
        const val TAG = "RemoteApiDataSourceImpl"
    }

    override fun fetchWeatherForecast(param: Map<String, String>): Flow<ResultData<ForecastResponse>> {
        return flow {
            try {
                emit(ResultData.Loading(true))
                val response = remoteApiService.fetchWeatherForecast(param)
                emit(processNetworkResponse(response))
                emit(ResultData.Loading(false))
            } catch (e: Exception) {
                emit(ResultData.Loading(false))
                e.printStackTrace()
                throw e
            }
        }
    }


    private fun <T> processNetworkResponse(response: Response<T>): ResultData<T> {
        return when (response.isSuccessful) {
            true -> {
                Log.d(TAG, "API successful Response: ${response.body()}")
                ResultData.Success(response.body()!!)
            }
            else -> {
                val errorResponseString = response.errorBody()?.string()
                Log.d(TAG, "API failed Response: $errorResponseString")
                ResultData.Failure(
                    error = ErrorData(
                        error = errorResponseString?.toApiErrorResponse(),
                        apiErrorCode = response.code(),
                        apiErrorMessage = response.message()
                    )
                )
            }
        }
    }
}