package com.example.beaconexample.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.example.beaconexample.R
import com.example.beaconexample.receivers.ProximityReceiver

object NotificationUtils {
    fun createChannel(ctx: Context, id: String) {
        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notificationChannels.forEach { channel ->
            if (channel.id == id) {
                return
            }
        }

        manager.createNotificationChannel(
            NotificationChannel(
                id,
                "PANA Proximity Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "PANA Beacon"
                it.enableLights(true)
                it.lightColor = Color.YELLOW
                it
            })
    }

    fun getProximityNotification(ctx: Context, message: String): Notification {
        createChannel(ctx, "BEACON_PROXIMITY_CHANNEL")

        val dismissIntent = Intent(ctx, ProximityReceiver::class.java).apply {
            action = "dismissNotification"
        }
        val pendingIntent =
            PendingIntent.getBroadcast(ctx, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(ctx, "BEACON_PROXIMITY_CHANNEL")
            .setSmallIcon(R.drawable.show_password_icon)
            .setContentTitle("PANA Beacon")
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(R.drawable.ic_launcher_foreground, "Está bien", pendingIntent)

        return builder.build()
    }
}