package com.example.coffeeplantdiseasedetection.repository

import android.graphics.Bitmap

interface Repository {
    suspend fun predict(image: Bitmap?): Int?
    suspend fun updateStats(district: String, diseaseType: String)
}