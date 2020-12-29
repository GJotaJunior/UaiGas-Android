package com.uai.uaigas.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://ip:port"

    val instance: Api by lazy{
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(OkHttpClient()).build()

        retrofit.create(Api::class.java)
    }
}