package com.android.weatherapp.repositories

import com.android.weatherapp.datasources.UserPreferenceDataStore
import com.android.weatherapp.utils.Constants.LAST_SEARCHED_LOCATION
import com.android.weatherapp.utils.Constants.TEMP_UNIT
import com.android.weatherapp.utils.Constants.WIND_UNIT
import kotlinx.coroutines.flow.Flow

class UserPreferenceRepositoryImpl(private val userPreferenceDataStore: UserPreferenceDataStore) :
    UserPreferenceRepository {
    override suspend fun saveUserSelectedTempUnit(value: String) {
        userPreferenceDataStore.saveStringData(TEMP_UNIT, value)
    }

    override fun getSavedUserSelectedTempUnit(): Flow<String?> {
        return userPreferenceDataStore.getStringData(TEMP_UNIT)
    }

    override suspend fun saveUserSelectedWindUnit(value: String) {
        userPreferenceDataStore.saveStringData(WIND_UNIT, value)
    }

    override fun getSavedUserSelectedWindUnit(): Flow<String?> {
        return userPreferenceDataStore.getStringData(WIND_UNIT)
    }

    override suspend fun saveUserLastSearchedLocation(value: String) {
        userPreferenceDataStore.saveStringData(LAST_SEARCHED_LOCATION, value)
    }

    override fun getSavedUserLastSearchLocation(): Flow<String?> {
        return userPreferenceDataStore.getStringData(LAST_SEARCHED_LOCATION)
    }
}