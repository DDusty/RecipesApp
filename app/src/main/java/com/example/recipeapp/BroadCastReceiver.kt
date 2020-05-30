package com.example.recipeapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Sets up the notification
 */
class BroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder = NotificationCompat.Builder(context!!, "notify")
            .setSmallIcon(R.drawable.ic_cooking_icon_png_1) // not working well
            .setContentTitle("Swipe some recipe's!")
            .setContentText("Add some more recipe's to your list")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())
    }
}