package com.udacity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.udacity.service.NotificationService

class LoadingStatusApp:Application(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val channel=NotificationChannel(
            NotificationService.LOADING_STATUS_CHANNEL_ID,
            NotificationService.LOADING_STATUS_CHANNEL_NAME,
            NotificationService.IMPORTANCE
        )
        channel.description= NotificationService.DESCRIPTION

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }
}