package com.instiq.test_notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService(){

    val nofityId = 1000

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Log.e("APP", p0?.notification?.title)
//        Toast.makeText(this, p0.toString(), Toast.LENGTH_LONG).show()

        createNotificationChannel()
        makeNotification(p0?.notification?.title, p0?.notification?.body)

    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
    }

    fun makeNotification(text: String?, body: String?){

       val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
       }
       val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

       val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(android.R.drawable.arrow_down_float)
                .setContentTitle(text)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

       with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(nofityId, builder.build())
       }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel)
            val descriptionText = getString(R.string.notification_channel_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}