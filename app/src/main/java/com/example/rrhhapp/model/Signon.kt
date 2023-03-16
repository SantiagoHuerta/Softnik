package com.example.rrhhapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Signon(
    @SerializedName("token") var token: String,
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double,
    @SerializedName("date") var date: String,
    @SerializedName("type") var type: String
)
