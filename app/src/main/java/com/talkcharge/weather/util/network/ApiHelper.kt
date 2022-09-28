package com.talkcharge.weather.util.network

import com.talkcharge.weather.common.model.WeatherResponse
import com.talkcharge.weather.common.model.current.CurrentWeatherResponse

class ApiHelper {
    private val apiService: ApiService = NetworkClient.apiService

    suspend fun getWeatherData(
        lat: String,
        lon: String,
        appId: String,
        exclude: String = "minutely,alerts"
    ): WeatherResponse {
        return apiService.getWeatherData(lat, lon, appId, exclude)
    }

    suspend fun getCurrentWeatherData(lat: String, lon: String, appId: String): CurrentWeatherResponse {
        return apiService.getCurrentWeatherData(lat, lon, appId)
    }

}