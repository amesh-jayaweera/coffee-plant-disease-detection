package com.example.coffeeplantdiseasedetection.api

import com.example.coffeeplantdiseasedetection.model.DiseaseResult
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("/predict")
    suspend fun predict(@Part image: MultipartBody.Part): DiseaseResult
}