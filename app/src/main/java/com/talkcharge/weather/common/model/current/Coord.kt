package com.talkcharge.weather.common.model.current

import com.google.gson.annotations.SerializedName


data class Coord(

    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("lat") var lat: Double? = null

)