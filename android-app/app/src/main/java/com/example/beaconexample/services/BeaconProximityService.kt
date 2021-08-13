package com.example.beaconexample.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.estimote.proximity_sdk.api.*
import com.example.beaconexample.receivers.ProximityReceiver
import com.example.beaconexample.utils.LocalStorage
import com.google.gson.Gson

class BeaconProximityService : Service() {
    private lateinit var credentials: EstimoteCloudCredentials
    private lateinit var observationHandler: ProximityObserver.Handler
    private lateinit var proximityObserver: ProximityObserver
    private lateinit var proximityZone: ProximityZone
    private val gson = Gson()

    companion object {
        const val TAG = "[BeaconProximityService]"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        buildProximityObserver()
        buildProximityZone()
        startObservations()

        return START_NOT_STICKY
    }

    private fun buildProximityObserver() {
        credentials = EstimoteCloudCredentials(
            "example-pana-25w",
            "0f4ba32a5b9c21e7fe38a45bb5613286"
        )

        proximityObserver = ProximityObserverBuilder(this, credentials)
            .onError { err ->
                Log.e(TAG, "ProximityObserverBuilder error: $err")
            }
            .withLowLatencyPowerMode()
            .build()
    }

    private fun buildProximityZone() {
        proximityZone = ProximityZoneBuilder()
            .forTag("solicitud")
            .inNearRange()
            .onEnter { proximityContext ->
                sendProximityEvent("enter", proximityContext.deviceId)
            }
            .onExit { proximityContext ->
                sendProximityEvent("exit", proximityContext.deviceId)
            }
            .build()
    }

    private fun startObservations() {
        observationHandler = proximityObserver.startObserving(proximityZone)
    }

    private fun stopObservations() {
        LocalStorage.helpArrived = false
        if (::observationHandler.isInitialized) {
            try {
                observationHandler.stop()
            } catch (err: Exception) {
                Log.e(TAG, "[!] Error: $err")
            }
        }
    }

    private fun sendProximityEvent(event: String, deviceId: String) {
        sendBroadcast(Intent("beaconEvent").apply {
            putExtra("event", event)
            putExtra("beaconId", deviceId)
        })

        sendBroadcast(Intent(this, ProximityReceiver::class.java).apply {
            action = "beaconEvent"
            putExtra("event", event)
            putExtra("beaconId", deviceId)
        })
    }

    override fun onDestroy() {
        stopObservations()
        super.onDestroy()
    }
}