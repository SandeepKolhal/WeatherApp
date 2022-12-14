package com.android.weatherapp.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val data = request.url.newBuilder().addQueryParameter(
            "key", "bf6de53ebea24e7a8a171758221312"
        ).build()

        request =
            request.newBuilder().addHeader("Content-Type", "application/json").url(data).build()

        return chain.proceed(request)
    }
}