package com.example.nagwa_task.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nagwa_task.R
import com.example.nagwa_task.databinding.ActivityMainBinding
import com.example.nagwa_task.model.ResponseItem
import com.example.nagwa_task.ui.adapter.MediaAdapter
import com.example.nagwa_task.viewmodel.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val mediaviewmodel:MediaViewModel by viewModels()
    private lateinit var mediaadapter:MediaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupadapter()
        mediaviewmodel.getmedia()
        observedata()
    }
    fun observedata(){
        mediaviewmodel.medialivedata.observe(this, Observer {
            binding.apply {
                mediaadapter.setData(it as ArrayList<ResponseItem>)
            }
        })
        mediaviewmodel.loading.observe(this, Observer {
            if (it){
                binding.progress.visibility= View.VISIBLE
            }else{
                binding.progress.visibility= View.GONE

            }

        })
        mediaviewmodel.msg.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })

    }
    fun setupadapter(){
        mediaadapter= MediaAdapter(ArrayList()) { postion, item ->
            isStoragePermissionGranted()

        }

        binding.mediaRecyclerview.layoutManager= LinearLayoutManager(this)


        binding.mediaRecyclerview.adapter=mediaadapter

    }
    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("TAG", "Permission is granted")
                true
            } else {
                Log.v("TAG", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else {
            Log.v("TAG", "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }
}