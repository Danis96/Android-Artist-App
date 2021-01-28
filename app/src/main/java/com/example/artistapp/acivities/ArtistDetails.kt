package com.example.artistapp.acivities
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.artistapp.ApiRequests
import com.example.artistapp.R
import com.example.artistapp.models.AlbumsItem
import com.example.artistapp.utils.Constants
import kotlinx.android.synthetic.main.activity_artist_details.*
import kotlinx.android.synthetic.main.activity_artist_details.progressBar1
import kotlinx.android.synthetic.main.artist_list_card.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class ArtistDetails : AppCompatActivity() {

    private val context: Context = this
    private var artistAlbums = arrayListOf<AlbumsItem>()
    private var artistAlbumAdapter: AlbumAdapter? = null
    var name: String? = ""
    var img: String? = ""
    var desc: String? = ""
    var albumLink: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_details)

        getData()
        getAllAlbums(context)
        artistAlbumAdapter = AlbumAdapter(context, artistAlbums)
        artistAlbumList.adapter = artistAlbumAdapter

    }


    private fun getData() {
        val bundle: Bundle = intent.extras!!
        name = bundle.getString("name")
        img = bundle.getString("image")
        desc = bundle.getString("desc")
        albumLink = bundle.getString("albums")

        tvArtistDetailsName.text = name
        tvArtistDetailsDesc.text = desc
        Glide.with(context).load(img).into(imgArtistDetails)
    }

    private fun getAllAlbums(context: Context) {
        progressBar1.isVisible = true
        val api = Retrofit.Builder()
            .baseUrl(Constants.BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiRequests::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getAlbums(albumLink!!).awaitResponse()
                if (response.isSuccessful) {
                    val dataList = response.body()!!
                    for (album in dataList) {
                        artistAlbums.add(album)
                    }
                    runOnUiThread {
                        progressBar1.isVisible = false
                        artistAlbumAdapter?.notifyDataSetChanged()
                    }
                }
            } catch (ex: Exception) {
                progressBar1.isVisible = false
                Log.d("ARTIST_EXCEPTION", ex.toString())
            }
        }
    }


    inner class AlbumAdapter(context: Context, private var listAlbum: ArrayList<AlbumsItem>):BaseAdapter() {

        private var context:Context? = context

        override fun getCount(): Int {
           return  listAlbum.size
        }

        override fun getItem(index: Int): Any {
          return listAlbum[index]
        }

        override fun getItemId(index: Int): Long {
            return index.toLong()
        }

        override fun getView(index: Int, convertView: View?, parent: ViewGroup?): View {

            val album = listAlbum[index]
            val inflator =
                context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val myView = inflator.inflate(R.layout.artist_list_card, null)

            myView.tvName.text = album.name
            myView.tvDescription.text = album.published.toString()
            Glide.with(context!!).load(album.cover).into(myView.imgName)

            myView.setOnClickListener {
                val intent = Intent(context, AlbumDetails::class.java)
                intent.putExtra("name", album.name)
                intent.putExtra("img", album.cover)
                intent.putExtra("song_num", album.songs_num)
                intent.putExtra("published", album.published)
                intent.putExtra("songs", album.songs)
                context!!.startActivity(intent)
            }

            return myView

        }

    }
}