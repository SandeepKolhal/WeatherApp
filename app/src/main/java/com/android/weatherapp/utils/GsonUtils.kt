package com.android.weatherapp.utils

import com.google.gson.Gson

object GsonUtils {

    inline fun <reified T> fromJson(jsonString: String?, clazz: Class<T>?): T? =
        Gson().fromJson(jsonString, clazz)

    inline fun <reified T> toJson(src: T): String = Gson().toJson(src)

}