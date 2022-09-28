package com.talkcharge.weather.common.model

import com.google.gson.annotations.SerializedName


data class Current(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("sunrise") var sunrise: Int? = null,
    @SerializedName("sunset") var sunset: Int? = null,
    @SerializedName("temp") var temp: Double? = null,
    @SerializedName("feels_like") var feelsLike: Double? = null,
    @SerializedName("pressure") var pressure: Int? = null,
    @SerializedName("humidity") var humidity: Int? = null,
    @SerializedName("dew_point") var dewPoint: Double? = null,
    @SerializedName("uvi") var uvi: Double? = null,
    @SerializedName("clouds") var clouds: Int? = null,
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind_speed") var windSpeed: Double? = null,
    @SerializedName("wind_deg") var windDeg: Double? = null,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    var iconUrl: String? = null,
    var feelsLikeWUnits: String? = null,
    var gistDesc: String? = null,
    var tempWUnits: String? = null,
    var pressureWUnits: String? = null,
    var windSpeedWUnits: String? = null,
    var windDirWUnits: String? = null
)