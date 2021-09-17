package com.example.prototipobeacons

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(auth: Boolean = false) : Interceptor {
    val authenticated = auth
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val request = request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")

        if(authenticated) {
            request.addHeader("Authorization", "Bearer ${LocalStorage.accessToken}")
        }

        proceed(
            request.build()
        )
    }
}