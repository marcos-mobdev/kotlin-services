package br.com.appforge.kotlin_service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class CustomApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    fun createNotificationChannels(){
        val channelId = "reminders"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
            //getSystemService(NotificationManager::class.java).createNotificationChannels(listOf(channel,channel))
            //getSystemService(NotificationManager::class.java).createNotificationChannelGroup()
            //getSystemService(NotificationManager::class.java).createNotificationChannelGroups()
        }
    }
}