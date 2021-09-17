package com.example.prototipobeacons

import android.app.Application
import android.content.Context
import android.util.Log

class BeaconApplication: Application(){

    companion object {
        lateinit  var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }

    override fun onLowMemory() {
        super.onLowMemory()

        Log.i("[BeaconApplication]", "[!] LOW MEMORY!")
    }
}