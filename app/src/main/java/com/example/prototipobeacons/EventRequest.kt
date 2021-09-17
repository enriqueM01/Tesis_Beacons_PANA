package com.example.prototipobeacons

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventRequest {
    @SerializedName("evento")
    @Expose
    var evento: String? = null

    @SerializedName ("hora")
    @Expose
    var hora: String? = null
}