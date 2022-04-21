package com.example.nagwa_task.repository


import android.graphics.Bitmap
import android.util.Log
import com.example.nagwa_task.model.ResponseItem
import com.example.nagwa_task.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class HomeRepository @Inject constructor(val apiService: ApiService) {

    suspend fun getmedia(): Flow <Response<List<ResponseItem>>> {
        return flow {
            emit(apiService.getmedia())
        }.flowOn(Dispatchers.IO)
    }


}