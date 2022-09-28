package com.talkcharge.weather.forecast.viewmodel.repository

import com.talkcharge.weather.R
import com.talkcharge.weather.WeatherApp
import com.talkcharge.weather.common.model.WeatherResponse
import com.talkcharge.weather.common.model.current.CurrentWeatherResponse
import com.talkcharge.weather.util.network.ApiHelper

class WeatherRepository {

    companion object {
        val appId: String = WeatherApp.getResourceString(R.string.app_key)
    }

    suspend fun callWeatherApi(lat: String, lon: String): WeatherResponse {
        val apiHelper = ApiHelper()
        return apiHelper.getWeatherData(lat, lon, appId)
    }

    suspend fun callCurrentWeatherApi(lat: String, lon: String): CurrentWeatherResponse {
        val apiHelper = ApiHelper()
        return apiHelper.getCurrentWeatherData(lat, lon, appId)
    }
}