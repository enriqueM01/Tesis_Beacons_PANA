package com.example.prototipobeacons.api

import com.example.prototipobeacons.EventRequest
import com.example.prototipobeacons.EventResponse
import com.example.prototipobeacons.LogoutRequest
import com.example.prototipobeacons.LogoutResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LogoutApi {

    @Headers("Content-Type:application/json")
    @POST("api/logout")
    fun registerlogout(
        @Body eventRequest: LogoutRequest
    ): Call<LogoutResponse>
}