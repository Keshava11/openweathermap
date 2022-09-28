package com.talkcharge.weather.util.network

import com.talkcharge.weather.common.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String,
        @Query("exclude") exclude: String
    ): WeatherResponse

//    // TODO Remove the following method
//    @GET
//    suspend fun getWeatherData(@Url url: String): WeatherResponse

}