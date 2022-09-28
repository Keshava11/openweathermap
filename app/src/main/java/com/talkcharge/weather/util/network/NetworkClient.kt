package com.talkcharge.weather.util.network

import com.talkcharge.weather.util.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient = run {
        val okHttpClientBuilder = OkHttpClient.Builder()
        if (AppConstants.logDebug)
            okHttpClientBuilder.addNetworkInterceptor(interceptor)
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        okHttpClientBuilder.build()
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

}