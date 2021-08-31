package com.example.beaconexample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.beaconexample.R
import com.example.beaconexample.utils.LocalStorage

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        var intent = Intent(this, LoginActivity::class.java)

        // If exists a session, go to Main Activity
        if (LocalStorage.activeUserSession) {
            intent = Intent(this, MainActivity::class.java)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        overridePendingTransition(0, 0)
        startActivity(intent)

        overridePendingTransition(0, 0)
        finish()
    }
}