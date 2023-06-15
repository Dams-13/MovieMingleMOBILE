package com.example.tp2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.2.125:3333/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofitInstance().create(ApiService::class.java)
}