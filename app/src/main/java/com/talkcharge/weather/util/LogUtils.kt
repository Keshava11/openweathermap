package com.talkcharge.weather.util

import android.util.Log

object LogUtils {

    fun v(iTag: String, iMessage: String) {
        if (AppConstants.logDebug) Log.v(iTag, iMessage)
    }

    fun d(iTag: String, iMessage: String) {
        if (AppConstants.logDebug) Log.d(iTag, iMessage)
    }

    fun w(iTag: String, iMessage: String) {
        if (AppConstants.logDebug) Log.w(iTag, iMessage)
    }

    fun e(iTag: String, iMessage: String) {
        if (AppConstants.logDebug) Log.e(iTag, iMessage)
    }

    fun i(iTag: String, iMessage: String) {
        if (AppConstants.logDebug) Log.i(iTag, iMessage)
    }

    fun e(iTag: String, iMessage: String, exception: Throwable) {
        if (AppConstants.logDebug) Log.e(iTag, iMessage, exception)
    }
}