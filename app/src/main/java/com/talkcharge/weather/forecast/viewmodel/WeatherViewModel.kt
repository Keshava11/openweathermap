package com.talkcharge.weather.forecast.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talkcharge.weather.common.model.Resource
import com.talkcharge.weather.common.model.WeatherResponse
import com.talkcharge.weather.forecast.viewmodel.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val weatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    private var lat: String
    private var lon: String

    init {
        //setting some defaults
        lat = "28.4817"
        lon = "77.1873"
        loadWeatherData()
    }

    private fun loadWeatherData() {

        viewModelScope.launch {

            weatherResponse.postValue(Resource.loading(null))

            try {
                val usersFromApi = repository.callWeatherApi(lat, lon)
                weatherResponse.postValue(Resource.success(usersFromApi))
            } catch (e: Exception) {
                weatherResponse.postValue(Resource.error(e.toString(), null))
            }

        }

    }

    fun getWeatherResponse(lat: String, lon: String): LiveData<Resource<WeatherResponse>> {
        this.lat = lat
        this.lon = lon

        // refresh the data with updated lat-lon
        loadWeatherData()

        return weatherResponse
    }


}