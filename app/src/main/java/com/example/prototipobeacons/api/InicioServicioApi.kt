package com.example.prototipobeacons.api

import com.example.prototipobeacons.EventRequest
import com.example.prototipobeacons.EventResponse
import com.example.prototipobeacons.InicioServicioRequest
import com.example.prototipobeacons.InicioServicioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface InicioServicioApi {

    @Headers("Content-Type:application/json")
    @POST("api/inicioservicio")
    fun registerinicio(
        @Body eventRequest: InicioServicioRequest
    ): Call<InicioServicioResponse>


}