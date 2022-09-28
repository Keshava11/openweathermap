package com.talkcharge.weather.util.network

import com.talkcharge.weather.common.model.WeatherResponse
import com.talkcharge.weather.common.model.current.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String,
        @Query("exclude") exclude: String
    ): WeatherResponse

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String
    ): CurrentWeatherResponse

}