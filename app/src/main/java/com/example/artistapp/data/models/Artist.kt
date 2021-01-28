package com.example.artistapp.data.models

import com.google.gson.annotations.SerializedName

class Artist(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("albums")
    val albums: String,
    @SerializedName("decription")
    val description: String,
)