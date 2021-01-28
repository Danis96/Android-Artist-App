package com.example.artistapp

import com.example.artistapp.models.Albums
import com.example.artistapp.models.Artists
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiRequests {

    @GET("f30a2517-60b4-11eb-a1e4-475264cf2bca")
    fun getArtists(): Call<Artists>

    @GET("{albumLink}")
    fun getAlbums(@Path("albumLink") name: String): Call<Albums>
}