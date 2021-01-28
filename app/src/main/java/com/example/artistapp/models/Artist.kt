package com.example.artistapp.models

import com.google.gson.annotations.SerializedName

class Artist(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("albums")
    val albums: List<Any>,
    @SerializedName("description")
    val description: String,
)