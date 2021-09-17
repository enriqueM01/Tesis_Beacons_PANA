package com.example.prototipobeacons

import android.content.Context
import android.content.SharedPreferences

object LocalStorage {
    private val preferences: SharedPreferences? =
        BeaconApplication.appContext.getSharedPreferences(
            "beacons.pana.localstorage",
            Context.MODE_PRIVATE
        )

    var accessToken: String
        get() = preferences!!.getString("accessToken", "")!!
        set(value) = preferences!!.edit().putString("accessToken", value).apply()

    var name: String
        get() = preferences!!.getString("name", "")!!
        set(value) = preferences!!.edit().putString("name", value).apply()
}