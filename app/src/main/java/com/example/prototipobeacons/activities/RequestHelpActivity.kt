package com.example.prototipobeacons.activities

import android.Manifest
import java.time.Instant
import java.time.format.DateTimeFormatter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.estimote.proximity_sdk.api.*
import com.example.prototipobeacons.*
import com.example.prototipobeacons.api.*
import com.example.prototipobeacons.databinding.ActivityRequestHelpBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.ZoneId
import java.util.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class RequestHelpActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var binding: ActivityRequestHelpBinding

    private lateinit var credentials: EstimoteCloudCredentials
    private lateinit var observationHandler: ProximityObserver.Handler
    private lateinit var proximityObserver: ProximityObserver
    private lateinit var proximityZone: ProximityZone
    private var proximityServiceStarted = false
    private val gson = Gson()


    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        Log.i(TAG, "PERMISO DE UBICACION ACCEDIDO")
                    }
                }
            }
        }

    private val callback = object : Callback<EventResponse> {
        override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
            Log.i("[SUCCESS]", gson.toJson(response))
        }

        override fun onFailure(call: Call<EventResponse>, t: Throwable) {
            Log.i("[FAILURE]", gson.toJson(t))
        }
    }

    private val callbackinicio = object : Callback<InicioServicioResponse> {
        override fun onResponse(
            call: Call<InicioServicioResponse>,
            response: Response<InicioServicioResponse>
        ) {
            Log.i("[SUCCESS]", gson.toJson(response))
        }

        override fun onFailure(call: Call<InicioServicioResponse>, t: Throwable) {
            Log.i("[FAILURE]", gson.toJson(t))
        }
    }

    private val callbackfin = object : Callback<FinServicioResponse> {
        override fun onResponse(
            call: Call<FinServicioResponse>,
            response: Response<FinServicioResponse>
        ) {
            Log.i("[SUCCESS]", gson.toJson(response))
        }

        override fun onFailure(call: Call<FinServicioResponse>, t: Throwable) {
            Log.i("[FAILURE]", gson.toJson(t))
        }
    }

    private val callbackayuda = object : Callback<AyudaResponse> {
        override fun onResponse(call: Call<AyudaResponse>, response: Response<AyudaResponse>) {
            Log.i("[SUCCESS]", gson.toJson(response))
        }

        override fun onFailure(call: Call<AyudaResponse>, t: Throwable) {
            Log.i("[FAILURE]", gson.toJson(t))
        }
    }

    private val callbacklogout = object : Callback<LogoutResponse> {
        override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
            Log.i("[SUCCESS]", gson.toJson(response))
        }

        override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
            Log.i("[FAILURE]", gson.toJson(t))
        }
    }

    companion object {
        const val TAG = "[RequestHelpActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRequestHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mensaje.text = "Bienvenido ${LocalStorage.name}"

        requestPermissions()

        buildProximityObserver()
        buildProximityZone()
        startObservations()

        binding.requestServiceBtn.setOnClickListener { view ->
            if (!proximityServiceStarted) {
                val request = InicioServicioRequest()
                request.hora = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("America/Caracas"))
                    .format(Instant.now())
                val retro = RetrofitClientInstance().getRetrofitClientInstance(true)
                    .create(InicioServicioApi::class.java)
                retro.registerinicio(request).enqueue(callbackinicio)
                //startObservations()
                proximityServiceStarted = true
                binding.requestServiceBtn.text = "finalizar servicio"
                binding.requestHelpBtn.visibility = View.VISIBLE
            } else {
                val request = FinServicioRequest()
                request.hora = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("America/Caracas"))
                    .format(Instant.now())
                val retro = RetrofitClientInstance().getRetrofitClientInstance(true)
                    .create(FinServicioApi::class.java)
                retro.registerfin(request).enqueue(callbackfin)
                //stopObservations()
                proximityServiceStarted = false
                binding.requestServiceBtn.text = "iniciar servicio"
                binding.requestHelpBtn.visibility = View.GONE
            }

            Snackbar.make(
                view,
                "La ayuda está en camino!",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
            Log.i(TAG, "Usuario pide Ayuda")
        }

        binding.requestHelpBtn.setOnClickListener { view ->
            Snackbar.make(
                view,
                "El centro de control te llamará pronto",
                Snackbar.LENGTH_LONG
            ).setAction("Action", null).show()
            val request = AyudaRequest()
            request.hora = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("America/Caracas"))
                .format(Instant.now())
            val retro = RetrofitClientInstance().getRetrofitClientInstance(true)
                .create(AyudaApi::class.java)
            retro.registerayuda(request).enqueue(callbackayuda)
        }

        binding.logoutBtn.setOnClickListener {
            val intent = Intent(this@RequestHelpActivity, Login::class.java)
            startActivity(intent)
            finish()

            Log.i(TAG, "Usuario se Deslogeo")
            val request = LogoutRequest()
            request.hora = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("America/Caracas"))
                .format(Instant.now())
            val retro = RetrofitClientInstance().getRetrofitClientInstance(true)
                .create(LogoutApi::class.java)
            retro.registerlogout(request).enqueue(callbacklogout)

        }
    }

    private fun requestPermissions() {
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
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
        val request = EventRequest()

        proximityZone = ProximityZoneBuilder()
            .forTag("zona")
            .inFarRange()
            .onEnter {
                Log.i(TAG, "BEACON DETECTADO!!")

                binding.mensaje.text = "${LocalStorage.name} ya estás en la zona PANA "
                binding.imagen.setImageResource(R.drawable.imagenzonin)

                request.evento = "DENTRO de la zona"
                request.hora = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("America/Caracas"))
                    .format(Instant.now())
                val retro = RetrofitClientInstance().getRetrofitClientInstance(true)
                    .create(EventApi::class.java)
                retro.registerEvent(request).enqueue(callback)
            }
            .onExit {
                Log.i(TAG, "EL BEACON SE ALEJAAAAA")

                binding.mensaje.text = "${LocalStorage.name} te estás alejando de la ayuda"
                binding.imagen.setImageResource(R.drawable.imagenzoneout)

                request.evento = "FUERA de la zona"
                request.hora = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("America/Caracas"))
                    .format(Instant.now())

                val retro = RetrofitClientInstance().getRetrofitClientInstance(true)
                    .create(EventApi::class.java)
                retro.registerEvent(request).enqueue(callback)
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
        stopObservations()

        super.onDestroy()
    }
}