package com.example.artistapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.artistapp.models.Artist
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.artist_list_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://jsonblob.com/api/jsonBlob/"

class MainActivity : AppCompatActivity() {

    var artistsList = arrayListOf<Artist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
            getAllArtists(this)

    }

//    private fun  requestStoragePermission() {
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100)
//        }
//
//    }


    private fun getAllArtists(context: Context) {
        val api =
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getArtists().awaitResponse()
                if (response.isSuccessful) {
                    val dataList = response.body()!!
                    for (artist in dataList.artists) {
                        artistsList.add(artist)
                    }
                 runOnUiThread {
                     val adapter = ArtistAdapter(context, artistsList)
                     artistListView.adapter = adapter
                 }
                }
            } catch (ex: Exception) {
                Log.d("ARTIST_EXCEPTION", ex.toString())
            }
        }
    }

    inner class ArtistAdapter(context: Context, private var listArtist: ArrayList<Artist>) :
        BaseAdapter() {

        private var context: Context? = context

        override fun getCount(): Int {
            return listArtist.size
        }

        override fun getItem(position: Int): Any {
            return listArtist[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {
            val artist = listArtist[index]
            val inflator =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val myView = inflator.inflate(R.layout.artist_list_card, null)

            myView.tvName.text = artist.name
            myView.tvDescription.text = artist.description
            Glide.with(context!!).load(artist.image).into(myView.imgName)
            return myView
        }

    }


}