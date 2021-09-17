package com.example.prototipobeacons.api

import com.example.prototipobeacons.HeaderInterceptor
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import okhttp3.OkHttpClient

class RetrofitClientInstance {
    private val BASE_URL: String = "https://beacon.stag.panatech.io/"

    fun getRetrofitClientInstance(auth: Boolean = false): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        val client = OkHttpClient.Builder().addInterceptor(HeaderInterceptor(auth)).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


}