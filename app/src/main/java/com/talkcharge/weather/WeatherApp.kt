package com.talkcharge.weather

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

@Suppress("unused")
class WeatherApp : Application() {

    companion object {
        private lateinit var appContext: Context
        private lateinit var preferences: SharedPreferences

        fun getResourceString(@StringRes resId: Int): String {
            return appContext.getString(resId)
        }

        fun getAppContext(): Context {
            return appContext
        }

        fun getAppPreferences(): SharedPreferences {
            return preferences
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        preferences = getSharedPreferences("weatherme", Context.MODE_PRIVATE)

    }


}