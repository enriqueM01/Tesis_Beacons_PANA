package com.example.prototipobeacons.api

import com.example.prototipobeacons.EventRequest
import com.example.prototipobeacons.EventResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface EventApi {
    @Headers("Content-Type:application/json")
    @POST("api/evento")
    fun registerEvent(
        @Body eventRequest: EventRequest
    ): Call<EventResponse>
}