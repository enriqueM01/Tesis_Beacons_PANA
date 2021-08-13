package com.example.beaconexample.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.beaconexample.MainApplication
import com.example.beaconexample.data.User
import com.google.gson.Gson

object LocalStorage {
    private val storage: SharedPreferences = MainApplication.appContext.getSharedPreferences(
        "pana_beacon_storage",
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    var activeUserSession: Boolean
        get() = storage.getBoolean("activeUserSession", false)
        set(value) = storage.edit().putBoolean("activeUserSession", value).apply()

    var user: User?
        get() {
            return if (storage.getString("user", "").isNullOrBlank()) {
                return null
            } else {
                gson.fromJson(storage.getString("user", ""), User::class.java)
            }
        }
        set(value) = storage.edit().putString("user", gson.toJson(value)).apply()

    var helpArrived: Boolean
        get() = storage.getBoolean("helpArrived", false)
        set(value) = storage.edit().putBoolean("helpArrived", value).apply()
}