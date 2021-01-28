package com.example.artistapp.acivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.artistapp.R
import kotlinx.android.synthetic.main.activity_album_details.*

class AlbumDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)

        getData()
    }

    private fun getData() {

        val bundle:Bundle = intent.extras!!
        val name = bundle.getString("name")
        val songNumber = bundle.getInt("song_number")
        val published = bundle.getInt("published")
        val cover = bundle.getString("img")
        val songs = bundle.getString("songs")

        tvAlbumDetailsName.text = name
        tvAlbumDetailsPublished.text = published.toString()
        tvAlbumDetailsSongs.text = songs
        Glide.with(this).load(cover).into(imgAlbumDetails)


    }
}