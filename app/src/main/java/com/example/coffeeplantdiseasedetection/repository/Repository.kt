package com.example.coffeeplantdiseasedetection.repository

import android.graphics.Bitmap
import com.example.coffeeplantdiseasedetection.model.BlogItem
import com.example.coffeeplantdiseasedetection.model.DiseaseStat

interface Repository {
    suspend fun predict(image: Bitmap?): Int?
    suspend fun updateStats(district: String, diseaseType: String)

    suspend fun blogs(): List<BlogItem>

    suspend fun statistics(): List<DiseaseStat>
}