package com.example.prototipobeacons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FinServicioResponse {
    @SerializedName("message")
    @Expose
    var message: String? = null
}