package com.example.prototipobeacons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AyudaRequest {
    @SerializedName("hora")
    @Expose
    var hora: String? = null
}