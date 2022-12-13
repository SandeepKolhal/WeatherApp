package com.android.weatherapp.network

import com.android.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteApiClient {
    private fun getAdapter() = Retrofit.Builder().apply {
        baseUrl(BuildConfig.BASE_URL)
        addConverterFactory(GsonConverterFactory.create())
        client(createOkHttpClient())
    }.build()

    private fun createOkHttpClient() = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        addInterceptor(AuthorizationInterceptor())
        connectTimeout(300000, TimeUnit.MILLISECONDS)
        readTimeout(300000, TimeUnit.MILLISECONDS)
        writeTimeout(300000, TimeUnit.MILLISECONDS)
    }.build()

    private val remoteApiService = getAdapter().create(RemoteApiService::class.java)

    fun getRemoteApiService(): RemoteApiService = remoteApiService
}