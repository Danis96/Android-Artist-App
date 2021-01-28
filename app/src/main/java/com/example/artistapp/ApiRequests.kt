package com.example.artistapp

import com.example.artistapp.models.Artists
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {

    @GET("f30a2517-60b4-11eb-a1e4-475264cf2bca")
    fun getArtists(): Call<Artists>
}