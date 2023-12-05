package com.example.coffeeplantdiseasedetection.api

import com.example.coffeeplantdiseasedetection.model.ApiSuccess
import com.example.coffeeplantdiseasedetection.model.BlogItem
import com.example.coffeeplantdiseasedetection.model.DiseaseResult
import com.example.coffeeplantdiseasedetection.model.DiseaseStat
import com.example.coffeeplantdiseasedetection.model.StatsRequestBody
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Api {

    @Multipart
    @POST("/predict")
    suspend fun predict(@Part image: MultipartBody.Part): DiseaseResult

    @POST("/update_count")
    suspend fun updateStats(@Body body: StatsRequestBody): ApiSuccess

    @GET("/blogs")
    suspend fun blogs(): List<BlogItem>

    @GET("/get_all_counts")
    suspend fun statistics(): List<DiseaseStat>
}