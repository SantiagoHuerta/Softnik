package com.example.rrhhapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class Signon(
    @SerializedName("date") var date: LocalDateTime,
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longitude") var longitude: Double,
    @SerializedName("token") var token: String
)
