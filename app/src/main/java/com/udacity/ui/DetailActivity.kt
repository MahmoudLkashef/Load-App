package com.udacity.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ContentDetailBinding
import com.udacity.utils.Constance

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var includedBinding: ContentDetailBinding
    private lateinit var fileName:String
    private var isDownloadSuccess:Boolean=false
    private var downloadId:Long=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        includedBinding=ContentDetailBinding.bind(binding.content.root)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val extrasData=intent.extras

        extrasData?.let {data->
            fileName= data.getString(Constance.FILE_NAME)!!
            isDownloadSuccess= data.getBoolean(Constance.DOWNLOAD_STATUS)
            downloadId=data.getLong(Constance.DOWNLOAD_ID)
        }

        with(includedBinding){
            tvFileNameResult.text=fileName
            if(!isDownloadSuccess){
                tvStatusResult.text="failed"
                tvStatusResult.setTextColor(Color.RED)
            }else{
                tvStatusResult.text="successful"
            }
            btnOk.setOnClickListener(View.OnClickListener {
                finish()
            })
        }
    }

}
