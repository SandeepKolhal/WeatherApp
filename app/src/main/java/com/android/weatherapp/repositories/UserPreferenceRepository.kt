package com.android.weatherapp.repositories

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun saveUserSelectedTempUnit(value: String)
    fun getSavedUserSelectedTempUnit(): Flow<String?>
    suspend fun saveUserSelectedWindUnit(value: String)
    fun getSavedUserSelectedWindUnit(): Flow<String?>
    suspend fun saveUserLastSearchedLocation(value: String)
    fun getSavedUserLastSearchLocation(): Flow<String?>
}