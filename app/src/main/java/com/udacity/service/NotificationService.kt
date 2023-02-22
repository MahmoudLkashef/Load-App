package com.udacity.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.ui.DetailActivity
import com.udacity.utils.Constance

class NotificationService(private val context:Context){
    companion object{
        const val LOADING_STATUS_CHANNEL_ID = "load_status_channel"
        const val LOADING_STATUS_CHANNEL_NAME = "LoadStatus"
        const val IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT
        const val DESCRIPTION = "Used display the loading status"
    }

    private val notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(id:Long,fileName:String,state:Boolean){
        val detailIntent=Intent(context,DetailActivity::class.java)
        detailIntent.putExtra(Constance.FILE_NAME,fileName)
        detailIntent.putExtra(Constance.DOWNLOAD_ID,id)
        detailIntent.putExtra(Constance.DOWNLOAD_STATUS,state)

        val activityPendingIntent=PendingIntent.getActivity(
            context,
            1,
            detailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification=NotificationCompat.Builder(context, LOADING_STATUS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_description))
            .setContentIntent(activityPendingIntent)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
            "Check the status",
                activityPendingIntent
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1,notification)
    }
}