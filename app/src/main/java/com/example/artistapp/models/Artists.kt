package com.example.artistapp.models

import com.google.gson.annotations.SerializedName

class Artists(
    @SerializedName("artists")
    val artists: List<Artist>
)