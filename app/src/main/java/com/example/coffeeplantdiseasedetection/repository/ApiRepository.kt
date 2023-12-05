package com.example.coffeeplantdiseasedetection.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.coffeeplantdiseasedetection.api.RetrofitClient
import com.example.coffeeplantdiseasedetection.model.BlogItem
import com.example.coffeeplantdiseasedetection.model.DiseaseStat
import com.example.coffeeplantdiseasedetection.model.StatsRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class ApiRepository : Repository {
    private val client = RetrofitClient.getApi()

    override suspend fun predict(image: Bitmap?): Int? {
        if (image != null) {
            val imageByteArray = ByteArrayOutputStream().use { bos ->
                image.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                bos.toByteArray()
            }

            val imagePart = MultipartBody.Part.createFormData("image", "image.jpg",
                RequestBody.create(MediaType.parse("image/*"), imageByteArray))

            return try {
                val result = client.predict(imagePart)
                result.category
            } catch (ex: Exception) {
                Log.e("ERROR", "Exception: " + ex.message)
                null
            }
        }
        return null
    }

    override suspend fun updateStats(district: String, diseaseType: String) {
        try {
            client.updateStats(StatsRequestBody(
                district,
                diseaseType))
        } catch (ex: Exception) {
            Log.e("ERROR", "Exception: " + ex.message)
        }
    }

    override suspend fun blogs(): List<BlogItem> {
        val items = mutableListOf<BlogItem>()
        return try {
            client.blogs()
        } catch (ex: Exception) {
            Log.e("ERROR", "Exception: " + ex.message)
            items
        }
    }

    override suspend fun statistics(): List<DiseaseStat> {
        val items = mutableListOf<DiseaseStat>()
        return try {
            client.statistics()
        } catch (ex: Exception) {
            Log.e("ERROR", "Exception: " + ex.message)
            items
        }
    }
}