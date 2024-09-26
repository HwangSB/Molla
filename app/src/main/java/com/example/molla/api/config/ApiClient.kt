package com.example.molla.api.config

import com.example.molla.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://${BuildConfig.SERVER_IP}:${BuildConfig.SERVER_PORT}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService: ApiService = retrofit.create(ApiService::class.java)
    }
}