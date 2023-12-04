package com.example.coffeeplantdiseasedetection.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.8.103:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(Api::class.java)

    fun getApi(): Api {
        return api
    }
}