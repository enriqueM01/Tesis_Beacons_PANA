package com.example.prototipobeacons.api

import com.example.prototipobeacons.EventRequest
import com.example.prototipobeacons.EventResponse
import com.example.prototipobeacons.FinServicioRequest
import com.example.prototipobeacons.FinServicioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FinServicioApi {
    @Headers("Content-Type:application/json")
    @POST("api/finservicio")
    fun registerfin(
        @Body eventRequest: FinServicioRequest
    ): Call<FinServicioResponse>
}