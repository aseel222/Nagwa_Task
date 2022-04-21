package com.example.nagwa_task.network



import com.example.nagwa_task.model.ResponseItem
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("example")
    suspend fun getmedia(
    ): Response<List<ResponseItem>>
}