package com.example.rrhhapp.io.response

data class SignonResponse(
    val code: Int,
    val message: String,
    val details: Array<String>
)