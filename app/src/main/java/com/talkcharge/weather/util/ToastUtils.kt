package com.talkcharge.weather.util

import android.content.res.Resources.NotFoundException
import android.text.TextUtils
import android.widget.Toast
import com.talkcharge.weather.WeatherApp

object ToastUtils {

    fun showToastDebug(iMessage: String?) {
        if (AppConstants.debug && !TextUtils.isEmpty(iMessage)) {
            Toast.makeText(WeatherApp.getAppContext(), iMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToast(iMessage: String?) {
        if (!TextUtils.isEmpty(iMessage)) {
            Toast.makeText(WeatherApp.getAppContext(), iMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun showToastLong(iMessage: String?) {
        if (!TextUtils.isEmpty(iMessage)) {
            Toast.makeText(WeatherApp.getAppContext(), iMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun showToastLong(iStringResId: Int) {
        if (iStringResId > 0) {
            try {
                val message = WeatherApp.getResourceString(iStringResId)
                Toast.makeText(WeatherApp.getAppContext(), message, Toast.LENGTH_LONG).show()
            } catch (ignored: NotFoundException) {
            }
        }
    }

    fun showToast(iStringResId: Int) {
        if (iStringResId > 0) {
            try {
                val message = WeatherApp.getResourceString(iStringResId)
                Toast.makeText(WeatherApp.getAppContext(), message, Toast.LENGTH_SHORT).show()
            } catch (ignored: NotFoundException) {
            }
        }
    }


}