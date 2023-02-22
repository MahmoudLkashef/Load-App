package com.udacity.service

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.udacity.model.ButtonState
import com.udacity.databinding.ContentMainBinding
import com.udacity.ui.MainActivity

class NotificationReceiver(private val binding: ContentMainBinding) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        id?.let {
            val isDownloadSuccess=downloadStatusHandler(it,context)
            //show notification
            val notificationService = NotificationService(context)
            notificationService.showNotification(
                it,
                MainActivity.fileName,
                isDownloadSuccess
            )
            binding.customButton.buttonState= ButtonState.Completed
        }

    }


    private fun downloadStatusHandler(id: Long, context: Context):Boolean{
        var downloadStatus=false
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val index=cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if(index!=-1){
                val status = cursor.getInt(index)
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL ->downloadStatus=true
                    else-> downloadStatus=false
                }
            }
        }
        return downloadStatus
    }
}