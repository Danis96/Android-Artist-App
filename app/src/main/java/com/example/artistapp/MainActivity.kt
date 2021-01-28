package com.example.artistapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.artistapp.acivities.ArtistDetails
import com.example.artistapp.data.ApiRequests
import com.example.artistapp.data.models.Artist
import com.example.artistapp.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.artist_list_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var artistsList = arrayListOf<Artist>()
    var artistListAdapter : ArtistAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllArtists(this)

        artistListAdapter = ArtistAdapter(this, artistsList)
        artistListView.adapter = artistListAdapter


    }

    private fun getAllArtists(context: Context) {
        progressBar1.isVisible = true
        val api =
            Retrofit.Builder()
                .baseUrl(Constants.BASE_API)
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
                        progressBar1.isVisible = false
                        artistListAdapter?.notifyDataSetChanged()
                    }
                }
            } catch (ex: Exception) {
                progressBar1.isVisible = false
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
//
            val artist = listArtist[index]
            val inflator =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val myView = inflator.inflate(R.layout.artist_list_card, null)

            myView.tvName.text = artist.name
            myView.tvDescription.text = artist.description
            Glide.with(context!!).load(artist.image).into(myView.imgName)

            myView.setOnClickListener {
                val intent = Intent(context, ArtistDetails::class.java)
                intent.putExtra("name", artist.name)
                intent.putExtra("desc", artist.description)
                intent.putExtra("image", artist.image)
                intent.putExtra("albums", artist.albums)
                context!!.startActivity(intent)
            }

            return myView
        }

    }


}