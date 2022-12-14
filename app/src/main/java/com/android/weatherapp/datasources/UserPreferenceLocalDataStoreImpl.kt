package com.android.weatherapp.datasources

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.android.weatherapp.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceLocalDataStoreImpl(private val context: Context) : UserPreferenceDataStore {
    override suspend fun saveStringData(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    override fun getStringData(key: Preferences.Key<String>) = context.dataStore.data.map {
        it[key]
    }

    override suspend fun saveIntData(key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit {
            it[key] = value
        }
    }

    override fun getIntData(key: Preferences.Key<Int>): Flow<Int?> = context.dataStore.data.map {
        it[key]
    }
}