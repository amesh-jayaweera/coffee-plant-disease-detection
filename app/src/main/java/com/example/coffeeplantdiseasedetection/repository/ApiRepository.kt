package com.example.coffeeplantdiseasedetection.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.coffeeplantdiseasedetection.api.RetrofitClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class ApiRepository : Repository {
    override suspend fun predict(image: Bitmap?): Int? {
        if (image != null) {
            val imageByteArray = ByteArrayOutputStream().use { bos ->
                image.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                bos.toByteArray()
            }

            val imagePart = MultipartBody.Part.createFormData("image", "image.jpg",
                RequestBody.create(MediaType.parse("image/*"), imageByteArray))

            return try {
                val result = RetrofitClient.getApi().predict(imagePart)
                result.category
            } catch (ex: Exception) {
                Log.e("ERROR", "Exception: " + ex.message)
                null
            }
        }
        return null
    }
}