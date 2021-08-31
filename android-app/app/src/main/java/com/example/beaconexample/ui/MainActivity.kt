package com.example.beaconexample.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.example.beaconexample.R
import com.example.beaconexample.databinding.ActivityMainBinding
import com.example.beaconexample.services.BeaconProximityService
import com.example.beaconexample.utils.LocalStorage
import com.example.beaconexample.viewmodels.UserViewModel
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()
    private var locationPermissionGranted = false
    private var bluetoothPermissionGranted = false
    private var pendingAction = false
    private lateinit var beaconServiceIntent: Intent
    private val gson = Gson()

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            permissions.entries.forEach {
                when (it.key) {
                    Manifest.permission.BLUETOOTH -> {
                        bluetoothPermissionGranted = it.value
                    }
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        locationPermissionGranted = it.value
                    }
                }

                if (bluetoothPermissionGranted && locationPermissionGranted && pendingAction) {
                    showCancelHelpLayer()
                    hideRequestHelpLayer()
                    startBeaconProximityService()
                }
            }
        }

    companion object {
        const val TAG = "[MainActivity]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.getUser()
        userViewModel.userData.observe(this, { user ->
            if (user != null) {
                binding.greetings.text =
                    String.format(resources.getText(R.string.greetings).toString(), user.firstName)
            }
        })

        registerReceiver(beaconProximityReceiver, IntentFilter("beaconEvent"))
    }

    override fun onStart() {
        super.onStart()

        RequirementsWizardFactory.createEstimoteRequirementsWizard().fulfillRequirements(
            this,
            onRequirementsFulfilled = { Log.i(TAG, "TODO BIENNNN") },
            onRequirementsMissing = { Log.e(TAG, "HAY PERMISOS FALTANTES") },
            onError = { Log.e(TAG, "ERROR SOLICITANDO PERMISOS") })

        LocalStorage.helpArrived = false

        binding.requestHelpBtn.setOnClickListener {
            if (!locationPermissionGranted || !bluetoothPermissionGranted) {
                pendingAction = true
                requestPermissions()
                return@setOnClickListener
            }

            hideRequestHelpLayer()
            showCancelHelpLayer()
            startBeaconProximityService()
        }

        binding.cancelHelpBtn.setOnClickListener {
            showRequestHelpLayer()
            hideCancelHelpLayer()
            stopBeaconProximityService()
        }

        binding.finishHelpBtn.setOnClickListener {
            showRequestHelpLayer()
            hideHelpArrivedLayer()
            hideCancelHelpLayer()
            stopBeaconProximityService()
        }
    }

    private fun checkLocationPermission() {
        val granted = PackageManager.PERMISSION_GRANTED
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == granted) {
            Log.i(TAG, "LOCATION GRANTED")
            locationPermissionGranted = true
            return
        }

        requestPermissions()
    }

    private fun requestPermissions() {
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    private fun startBeaconProximityService() {
        beaconServiceIntent = Intent(this, BeaconProximityService::class.java)
        startService(beaconServiceIntent)
    }

    private fun stopBeaconProximityService() {
        stopService(beaconServiceIntent)
    }

    private fun showHelpArrivedLayer() {
        binding.helpArrivedLayer.apply {
            translationY = 100f
            visibility = View.VISIBLE
            animate()
                .translationY(0f).setDuration(250)
                .alpha(1f).duration = 250
        }
    }

    private fun hideHelpArrivedLayer() {
        binding.helpArrivedLayer.apply {
            animate()
                .translationY(100f).setDuration(250)
                .alpha(0f).setDuration(150)
                .withEndAction {
                    visibility = View.INVISIBLE
                }
        }
    }

    private fun showCancelHelpLayer() {
        binding.cancelHelpLayer.apply {
            translationY = 100f
            visibility = View.VISIBLE
            animate()
                .translationY(0f).setDuration(250)
                .alpha(1f).setDuration(250)
                .startDelay = 250
        }
    }

    private fun hideCancelHelpLayer() {
        binding.cancelHelpLayer.apply {
            animate()
                .translationY(100f).setDuration(250)
                .alpha(0f).setDuration(150)
                .withEndAction {
                    visibility = View.INVISIBLE
                }
        }
    }

    private fun showRequestHelpLayer() {
        binding.requestHelpLayer.apply {
            animate().translationY(0f).setDuration(250).alpha(1f).duration = 250
        }
    }

    private fun hideRequestHelpLayer() {
        binding.requestHelpLayer.apply {
            animate().translationY(100f).setDuration(250).alpha(0f).duration = 150
        }
    }

    private val beaconProximityReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent!!.getStringExtra("event")) {
                "enter" -> {
                    hideCancelHelpLayer()
                    showHelpArrivedLayer()
                }
                "exit" -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(beaconProximityReceiver)
    }
}