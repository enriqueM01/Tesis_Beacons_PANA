package com.example.beaconexample.utils

import android.app.Activity
import android.content.Intent

fun <T> Activity.goToActivity(clazz: Class<T>) {
    val intent = Intent(this, clazz)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

    overridePendingTransition(0, 0)
    startActivity(intent)

    overridePendingTransition(0, 0)
    finish()
}