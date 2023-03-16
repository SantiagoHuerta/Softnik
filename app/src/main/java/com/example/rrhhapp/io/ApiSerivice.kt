package com.example.rrhhapp.io

import com.example.rrhhapp.io.response.LoginResponse
import com.example.rrhhapp.io.response.SignonResponse
import com.example.rrhhapp.model.Signon
import com.example.rrhhapp.model.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    companion object Factory{
        private const val BASE_URL = "http://54.172.25.110/"
        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @Headers("Content-Type: application/json")
    @POST( value = "auth/login")
    fun postLogin(@Body user: User): Call<LoginResponse>


    @Headers("Content-Type: application/json")
    @POST( value = "registry/signon")
    fun postSignon(@Header(value= "Authorization") authHeader: String, @Body signon: Signon): Call<SignonResponse>

}