package com.zafar.umair.rentoutadmin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class RentOutMessagingService : FirebaseMessagingService() {

    private val random = Random
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("NOTIFICATION", remoteMessage.data.toString())
        remoteMessage.notification?.let { message ->
            sendNotification(message)
            Log.d("NOTIFICATION", remoteMessage.data.toString())
        }
        super.onMessageReceived(remoteMessage)
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        // If you want the notifications to appear when your app is in foreground

        val intent = Intent(this, RentOutAdminActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = this.getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(random.nextInt(), notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null && !currentUser.isAnonymous) {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid)
                .update("fcmToken", token)
        }
        Log.d("FCM", "New token: $token")
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}