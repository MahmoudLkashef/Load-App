package com.udacity.ui

import android.app.DownloadManager
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.service.NotificationReceiver
import com.udacity.R
import com.udacity.model.ButtonState
import com.udacity.databinding.ActivityMainBinding
import com.udacity.databinding.ContentMainBinding


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var includedBinding: ContentMainBinding
    private lateinit var receiver:NotificationReceiver
    private var url = ""
    companion object{
        var fileName=""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        includedBinding = ContentMainBinding.bind(binding.content.mainLayout)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        receiver=NotificationReceiver(includedBinding)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        binding.content.customButton.setOnClickListener {
            radioGroupHandler(includedBinding.radioGroup.checkedRadioButtonId)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


    private fun download(url:String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)
    }

    private fun radioGroupHandler(checkId: Int) {
        when (checkId) {
            -1 -> {
                Toast.makeText(
                this@MainActivity,
                getString(R.string.toast_message),
                Toast.LENGTH_SHORT).show()
                return
            }

            R.id.radio_button_glide -> {
                url ="https://github.com/bumptech/glide"
                fileName=getString(R.string.glide_imageloading_library_by_bumptech)
            }

            R.id.radio_button_load_app -> {
                url =
                    "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                fileName=getString(R.string.loadapp_current_repository_by_udacity)
            }

            R.id.radio_button_retrofit ->{
                url ="https://github.com/square/retrofit"
                fileName=getString(R.string.retrofit_type_safe_http_client_for_android_and_java_by_square_inc)
            }

          }
        includedBinding.customButton.buttonState= ButtonState.Loading
        download(url)
    }


}
