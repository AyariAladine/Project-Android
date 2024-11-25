package com.example.bookproject.DataClass

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.MINUTES) // 3-minute connection timeout
        .readTimeout(3, TimeUnit.MINUTES)    // 3-minute read timeout
        .writeTimeout(3, TimeUnit.MINUTES)   // 3-minute write timeout
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
