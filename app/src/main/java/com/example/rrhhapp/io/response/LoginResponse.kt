package com.example.rrhhapp.io.response

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val tenant: String
)

