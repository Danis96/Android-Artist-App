package com.example.artistapp.acivities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.artistapp.R
import kotlinx.android.synthetic.main.activity_album_details.*

class AlbumDetails : AppCompatActivity() {

    var permissionsToRequest = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)

        getData()
    }

    private fun getData() {

        val bundle: Bundle = intent.extras!!
        val name = bundle.getString("name")
        val songNumber = bundle.getInt("song_number")
        val published = bundle.getInt("published")
        val cover = bundle.getString("img")
        val songs = bundle.getString("songs")

        tvAlbumDetailsName.text = name
        tvAlbumDetailsPublished.text = published.toString()
        tvAlbumDetailsSongs.text = songs
        Glide.with(this).load(cover).into(imgAlbumDetails)

        btnCAMERA.setOnClickListener {
            requestPermissionCamera()
        }

        btnLOCATION.setOnClickListener {
            requestPermissionLocation()
        }
    }


    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationForegroundPermission() =
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED


    private fun requestPermissionCamera() {
        if (!hasCameraPermission()) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 100)
        }
    }


    private fun requestPermissionLocation() {
        if (!hasLocationForegroundPermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions||", "${permissions[i]} granted")
                    permissionsToRequest.clear()
                }
            }
        } else if(requestCode == 101 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions||", "${permissions[i]} granted")
                    permissionsToRequest.clear()
                }
            }
        }
    }

}