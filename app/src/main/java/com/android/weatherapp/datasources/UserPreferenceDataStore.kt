package com.android.weatherapp.datasources

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDataStore {
    suspend fun saveStringData(key: Preferences.Key<String>, value: String)
    fun getStringData(key: Preferences.Key<String>): Flow<String?>
    suspend fun saveIntData(key: Preferences.Key<Int>, value: Int)
    fun getIntData(key: Preferences.Key<Int>): Flow<Int?>
}