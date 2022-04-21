package com.example.nagwa_task.network

import android.content.Context
import com.example.nagwa_task.ui.utils.NetworkHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiManager {
    @Singleton
    @Provides
    fun providehttpLoggerInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    }

    @Singleton
    @Provides
    fun provideCallFactory(httpLoggingInterceptor: HttpLoggingInterceptor): Call.Factory {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor {
                val request = it.request().newBuilder().build()


                return@addInterceptor it.proceed(request)
            }.connectTimeout(10,TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .readTimeout(30,TimeUnit.SECONDS)
            .build()
    }



    @Singleton
    @Provides
    fun provideBaseUrl(): String {
        return "http://5eb6de5d-5037-406e-bd53-5c43c152d171.mock.pstmn.io/"
    }


    @Singleton
    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelper(context)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        httpLoggingInterceptor: Call.Factory,
        gson: Gson,
        baseUrl: String
    ): Retrofit {

        return Retrofit.Builder()
            .callFactory(httpLoggingInterceptor)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()

    }



    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {

        return retrofit.create(ApiService::class.java)
    }
}