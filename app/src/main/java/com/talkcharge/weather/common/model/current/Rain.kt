package com.talkcharge.weather.common.model.current

import com.google.gson.annotations.SerializedName


data class Rain(

    @SerializedName("1h") var r1h: Double? = null

)