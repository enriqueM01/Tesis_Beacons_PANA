package com.example.prototipobeacons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("hora")
    @Expose
    var hora: String? = null
}