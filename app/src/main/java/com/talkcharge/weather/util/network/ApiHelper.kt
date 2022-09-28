package com.talkcharge.weather.util.network

import com.talkcharge.weather.common.model.WeatherResponse

class ApiHelper {
    private val apiService: ApiService = NetworkClient.apiService

    suspend fun getWeatherData(lat: String, lon: String, appId: String, exclude:String="minutely,alerts"): WeatherResponse {
        return apiService.getWeatherData(lat, lon, appId, exclude)
//        return apiService.getWeatherData("https://run.mocky.io/v3/36b375f6-7cca-49af-bb5e-e3fc428d938c")
    }

}