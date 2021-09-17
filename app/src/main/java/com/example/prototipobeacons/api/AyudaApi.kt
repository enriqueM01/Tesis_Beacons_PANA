package com.example.prototipobeacons.api

import com.example.prototipobeacons.AyudaRequest
import com.example.prototipobeacons.AyudaResponse
import com.example.prototipobeacons.EventRequest
import com.example.prototipobeacons.EventResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AyudaApi {
    @Headers("Content-Type:application/json")
    @POST("api/ayuda")
    fun registerayuda(
        @Body eventRequest: AyudaRequest
    ): Call<AyudaResponse>

}