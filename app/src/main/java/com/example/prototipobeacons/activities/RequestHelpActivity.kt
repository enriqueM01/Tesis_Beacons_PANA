package com.example.prototipobeacons.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.estimote.proximity_sdk.api.*
import com.example.prototipobeacons.R
import com.example.prototipobeacons.databinding.ActivityRequestHelpBinding

class RequestHelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestHelpBinding

    private lateinit var credentials: EstimoteCloudCredentials
    private lateinit var observationHandler: ProximityObserver.Handler
    private lateinit var proximityObserver: ProximityObserver
    private lateinit var proximityZone: ProximityZone
    private var proximityServiceStarted = false


    companion object {
        const val TAG = "[RequestHelpActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRequestHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buildProximityObserver()
        buildProximityZone()


        binding.requestServiceBtn.setOnClickListener { view ->
            if (!proximityServiceStarted) {
                startObservations()
                proximityServiceStarted = true
                binding.requestServiceBtn.text = "finalizar servicio"
                binding.requestHelpBtn.visibility = View.VISIBLE
            } else {
                stopObservations()
                proximityServiceStarted = false
                binding.requestServiceBtn.text = "iniciar servicio"
                binding.requestHelpBtn.visibility = View.GONE
            }

            Snackbar.make(
                view,
                "La ayuda está en camino! El centro de control te llamará pronto",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
            Log.i(TAG, "Usuario pide Ayuda")
        }

        binding.requestHelpBtn.setOnClickListener{ view ->
            Snackbar.make(
                view,
                "El centro de control te llamará pronto",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
        }

        registerReceiver(beaconBroadcastReceiver, IntentFilter("beacon.event"))
    }

    private fun buildProximityObserver() {
        credentials = EstimoteCloudCredentials(
            "tesispana-kjo",
            "f26e632c59a7f52b93df2d0fe68f3f4f"
        )

        proximityObserver = ProximityObserverBuilder(this, credentials)
            .onError { err ->
                Log.e(TAG, "ProximityObserverBuilder error: $err")
            }
            .withLowLatencyPowerMode()
            .onError { err -> Log.i(TAG, "ERROR STARTING OBSERVERRR: $err") }
            .build()
    }

    private fun buildProximityZone() {
        proximityZone = ProximityZoneBuilder()
            .forTag("zona")
            .inCustomRange(3.5)
            .onEnter {
                Log.i(TAG, "BEACON DETECTADO!!")
                val intent = Intent("beacon.event")
                intent.putExtra("action", "enter")
                sendBroadcast(intent)
            }
            .onExit {
                Log.i(TAG, "EL BEACON SE ALEJAAAAA")
                val intent = Intent("beacon.event")
                intent.putExtra("action", "exit")
                sendBroadcast(intent)
            }
            .onContextChange { context -> Log.i(TAG, "CONTEXT: $context") }
            .build()
    }

    private fun startObservations() {
        Log.i(TAG, "STARTING BEACON OBSERVATIONSSSS")
        observationHandler = proximityObserver.startObserving(proximityZone)
    }

    private fun stopObservations() {
        observationHandler.stop()
    }

    override fun onDestroy() {
        if (proximityServiceStarted) {
            observationHandler.stop()
        }

        unregisterReceiver(beaconBroadcastReceiver)

        super.onDestroy()
    }

    private val beaconBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.getStringExtra("action")) {
                "enter" -> {
                    Log.i(TAG, "AHORA ESTAS CON LA AYUDA!")
                    val enZona: TextView = findViewById(R.id.mensaje)
                    enZona.text = "SAFE, Estás en zona Pana"
                    val enZonaImagen1: ImageView = findViewById(R.id.imagen)
                    enZonaImagen1.setImageResource(R.drawable.imagenzonin)
                }
                "exit" -> {
                    Log.i(TAG, "TE ESTAS ALEJANDO DE LA AYUDA")
                    val enZona2: TextView = findViewById(R.id.mensaje)
                    enZona2.text = "La ayuda se está alejando"
                    val enZonaImagen1: ImageView = findViewById(R.id.imagen)
                    enZonaImagen1.setImageResource(R.drawable.imagenzoneout)
                }
            }
        }
    }
}