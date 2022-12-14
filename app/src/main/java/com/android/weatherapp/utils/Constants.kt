package com.android.weatherapp.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    val TEMP_UNIT = stringPreferencesKey("tempUnit")
    val WIND_UNIT = stringPreferencesKey("windUnit")
    val LAST_SEARCHED_LOCATION = stringPreferencesKey("lastSearchedLocation")
    const val PREFERENCES_NAME = "weatherApp_preferences"
    const val CELSIUS = "celsius"
    const val FAHRENHEIT = "fahrenheit"
    const val KMH = "km/h"
    const val MPH = "mph"
}