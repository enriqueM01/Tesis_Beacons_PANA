package com.example.beaconexample.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.beaconexample.utils.NotificationUtils

class ProximityReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "[NotificationReceiver]"
    }

    override fun onReceive(ctx: Context, intent: Intent?) {
        when (intent!!.action) {
            "beaconEvent" -> {
                when (intent.getStringExtra("event")) {
                    "enter" -> {
                        val notification = NotificationUtils.getProximityNotification(
                            ctx,
                            "¡La ayuda ha llegado!"
                        )
                        val manager =
                            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.notify(100, notification)
                    }
                    "exit" -> {
                        val notification = NotificationUtils.getProximityNotification(
                            ctx,
                            "Creemos que te estas alejando de la ayuda"
                        )
                        val manager =
                            ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        manager.notify(100, notification)
                    }
                }
            }
            "dismissNotification" -> {
                val manager =
                    ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancel(100)
            }
        }
    }
}