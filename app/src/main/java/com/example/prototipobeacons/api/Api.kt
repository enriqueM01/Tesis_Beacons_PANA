package com.example.prototipobeacons.api

import com.example.prototipobeacons.UserRequest
import com.example.prototipobeacons.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @Headers("Content-Type:application/json")
    @POST("api/login/")
    fun login(
        @Body userRequest: UserRequest): Call<UserResponse>
}
