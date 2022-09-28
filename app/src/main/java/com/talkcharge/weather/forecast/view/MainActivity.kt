package com.talkcharge.weather.forecast.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.permissionx.guolindev.PermissionX
import com.squareup.picasso.Picasso
import com.talkcharge.weather.R
import com.talkcharge.weather.common.model.Daily
import com.talkcharge.weather.common.model.Hourly
import com.talkcharge.weather.common.model.Status
import com.talkcharge.weather.common.model.WeatherResponse
import com.talkcharge.weather.common.model.current.CurrentWeatherResponse
import com.talkcharge.weather.databinding.ActivityMainBinding
import com.talkcharge.weather.forecast.viewmodel.WeatherViewModel
import com.talkcharge.weather.forecast.viewmodel.WeatherViewModelFactory
import com.talkcharge.weather.forecast.viewmodel.repository.WeatherRepository
import com.talkcharge.weather.util.LogUtils
import com.talkcharge.weather.util.ToastUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var hourlies = ArrayList<Hourly>()
    private var dailies = ArrayList<Daily>()
    private var dailyAdapter: DailyDataAdapter = DailyDataAdapter(dailies)
    private var hourlyAdapter: HourlyDataAdapter = HourlyDataAdapter(hourlies)

    private val viewModel: WeatherViewModel by lazy {
        val factory = WeatherViewModelFactory(WeatherRepository())
        ViewModelProvider(this, factory)[WeatherViewModel::class.java]
    }

    companion object {
        private const val CENT_SEC = 100L
        private const val LOCATION_PERMISSION_SETTINGS_CODE = 1729
        private val SDF_TIME_HH_MM = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        private val SDF_TIME_WEEKDAY_DD_MM = SimpleDateFormat("EEE\ndd/MM", Locale.ENGLISH)
        private const val ICON_URL_FORMAT_STR = "https://openweathermap.org/img/w/%s.png"
        private const val DEFAULT_ICON_URL = "https://openweathermap.org/img/w/04n.png"
        private const val WEATHER_DATA = "weather_data";
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // init Ui
        initUI()

        // Check permission and prompt to enable it
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList ->

                scope.showRequestReasonDialog(
                    deniedList, "App's core functionality is based on the permissions", "OK", "Cancel"
                )

            }.onForwardToSettings { scope, deniedList ->

                scope.showForwardToSettingsDialog(
                    deniedList, "You need to allow necessary permissions from Settings manually", "OK", "Cancel"
                )

            }.request { allGranted, grantedList, deniedList ->

                if (allGranted) {

                    ToastUtils.showToast("All permissions are granted")
                    // access location now
                    getCurrentLocation()

                } else {
                    ToastUtils.showToastLong("These permissions are denied: $deniedList")
                }

            }

    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = CENT_SEC
            fastestInterval = CENT_SEC
            numUpdates = 1
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)

        result.addOnCompleteListener { task: Task<LocationSettingsResponse> ->
            try {

                val response = task.getResult(ApiException::class.java)
                if (response.locationSettingsStates?.isLocationPresent == true) {
                    getLastLocation()
                } else {
                    LogUtils.e("TAG", "No valid location provider found")
                }

            } catch (exception: ApiException) {
                exception.printStackTrace()

                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        startIntentSenderForResult(
                            resolvable.resolution.intentSender, LOCATION_PERMISSION_SETTINGS_CODE, null, 0, 0, 0, null
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        failedToGetLocation()
                    } catch (e: ClassCastException) {
                        failedToGetLocation()
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // show error
                        failedToGetLocation()
                    }
                    else -> {
                        LogUtils.v("TAG", "Invalid status code on location fetch")
                    }
                }

            }
        }
    }


    private fun failedToGetLocation() {
        ToastUtils.showToast("Error getting location, please check your location settings or restart the app")
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        LogUtils.v("TAG", "Accessing last saved location if available ")

        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProvider.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {

                val location = task.result
                // location accessed, process further

                location?.let {
                    LogUtils.d(
                        "TAG", "Updated Location : " + "${location.latitude}, " + "${location.longitude}"
                    )

                    fetchWeatherData(location.latitude.toString(), location.longitude.toString())

                }

            } else {
                requestNewLocationData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = CENT_SEC
            fastestInterval = CENT_SEC
            numUpdates = 1
        }

        val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        Looper.myLooper()?.let {
            fusedLocationProvider.requestLocationUpdates(
                locationRequest, locationCallback, it
            )
        }

    }


    @Suppress("OVERRIDE_DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOCATION_PERMISSION_SETTINGS_CODE) {

            if (resultCode == RESULT_OK) {
                getCurrentLocation()

            } else {
                failedToGetLocation()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val lastLoc: Location? = locationResult.lastLocation
            // location accessed, process further

            lastLoc?.let {
                LogUtils.d("TAG", "Location Coordinates : " + "${lastLoc.latitude}, ${lastLoc.longitude}")

                fetchWeatherData(lastLoc.latitude.toString(), lastLoc.longitude.toString())

            }

        }
    }


    // Update the ViewModel to fetch
    private fun fetchWeatherData(lat: String, lon: String) {

        ToastUtils.showToastDebug("Calling weather api now")
        LogUtils.v("TAG", "Calling ViewModel to refresh the data again")

        viewModel.getWeatherResponse(lat, lon).observe(this) {
            when (it.status) {

                Status.SUCCESS -> {
                    binding.locationUpdatePb.visibility = View.GONE
                    it.data?.let { weatherResponse -> updateUI(weatherResponse) }
                }

                Status.LOADING -> {
                    binding.locationUpdatePb.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    binding.locationUpdatePb.visibility = View.GONE
                    LogUtils.v("TAG", "ViewModel error for weather data ${it.message}")
                }

            }
        }

        viewModel.getCurrentWeatherResponse(lat, lon).observe(this) {
            when (it.status) {

                Status.SUCCESS -> {
                    binding.locationUpdatePb.visibility = View.GONE
                    it.data?.let { weatherResponse -> updateHeader(weatherResponse) }
                }

                Status.LOADING -> {
                    binding.locationUpdatePb.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    binding.locationUpdatePb.visibility = View.GONE
                    LogUtils.v("TAG", "ViewModel error for current weather data ${it.message}")
                }

            }
        }

    }

    private fun updateHeader(weatherData: CurrentWeatherResponse) {
        try {
            ToastUtils.showToastDebug(
                "Current Weather Data received : ${weatherData.name}"
            )

            // top header
            val headerStr = "${weatherData.name} / (${weatherData.coord?.lat}, ${weatherData.coord?.lon})"
            binding.locationDetailTxv.text = headerStr
        } catch (ex: Exception) {
            ex.printStackTrace()
            LogUtils.e("TAG", "Error updating header UI")
        }
    }

    private fun updateUI(weatherData: WeatherResponse) {
        try {
            ToastUtils.showToastDebug(
                "Weather Data received : ${weatherData.lat}, ${weatherData.lon} " + "(${weatherData.timezone})"
            )

            // Get the current Data
            val currentData = weatherData.current
            currentData?.let {

                val temp = currentData.temp
                val feelsLike = currentData.feelsLike
                val weatherDt = currentData.weather
                val pressure = currentData.pressure
                val windDir = currentData.windDeg
                val windSpeed = currentData.windSpeed

                temp?.let {
                    val tmp = temp.minus(273.0)
                    currentData.tempWUnits = "%.2f °C".format(tmp)
                }

                feelsLike?.let {
                    val tmp = feelsLike.minus(273.0)
                    currentData.feelsLikeWUnits = "Feels like %.2f °C".format(tmp)
                }

                currentData.iconUrl = if (weatherDt.size > 0) String.format(ICON_URL_FORMAT_STR, weatherDt[0].icon)
                else DEFAULT_ICON_URL

                currentData.gistDesc = if (weatherDt.size > 0) weatherDt[0].description else " -- "

                pressure?.let {
                    currentData.pressureWUnits = "$pressure hpa"
                }

                windDir?.let {
                    currentData.windDirWUnits = degreeToDirection(windDir)
                }

                windSpeed?.let {
                    val ws = (windSpeed * 2.237).roundToInt()
                    currentData.windSpeedWUnits = "$ws mph"
                }

            }

            // Get the hourly list
            weatherData.hourly.forEach { h ->
                run {
                    val dt = h.dt?.times(1000L)
                    dt?.let {
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = dt
                        val date = cal.time
                        h.hourlyTimeStr = SDF_TIME_HH_MM.format(date)
                    }

                    val icUrl = if (h.weather.size > 0) String.format(
                        ICON_URL_FORMAT_STR, h.weather[0].icon
                    ) else DEFAULT_ICON_URL
                    h.iconUrl = icUrl

                    // Update the temp to celcius
                    h.temp?.let {
                        val tmp = h.temp?.minus(273.0)
                        h.tempWUnits = "%.2f °C".format(tmp)
                    }

                }
            }

            // Get the daily List
            weatherData.daily.forEach { d ->
                run {
                    val dt = d.dt?.times(1000L)
                    dt?.let {
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = dt
                        val date = cal.time
                        d.dayTimeStr = SDF_TIME_WEEKDAY_DD_MM.format(date)
                    }

                    val icUrl = if (d.weather.size > 0) String.format(
                        ICON_URL_FORMAT_STR, d.weather[0].icon
                    ) else DEFAULT_ICON_URL
                    d.iconUrl = icUrl

                    // Update the temp to celcius
                    d.temp?.let {
                        val tmp = it.day?.minus(273.0)
                        d.dailyTempWUnits = "%.2f °C".format(tmp)
                    }
                }
            }


            // Update the current UI
            if (currentData != null) {

                try {
                    Picasso.get().load(currentData.iconUrl).error(R.drawable.ic_weather_cloud)
                        .placeholder(R.drawable.ic_weather_cloud).into(binding.currentWeatherGistImv)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    LogUtils.e("TAG", "Error loading gist imv")
                }

                binding.currentTemperatureTxv.text = currentData.tempWUnits
                binding.currentFeelsLikeTxv.text = currentData.feelsLikeWUnits
                binding.currentWeatherGistTxv.text = currentData.gistDesc
                binding.windPressureValueTxv.text = currentData.pressureWUnits
                binding.windSpeedValueTxv.text = currentData.windSpeedWUnits
                binding.windDirValueTxv.text = currentData.windDirWUnits

            }

            hourlies = weatherData.hourly
            dailies = weatherData.daily

            // notify the adapters with updated data
            dailyAdapter.setDataSet(dailies)
            hourlyAdapter.setDataSet(hourlies)

            // Dataset is changed, schedule layout animation
            binding.dailyForecastRv.scheduleLayoutAnimation()
            binding.hourlyForecastRv.scheduleLayoutAnimation()

        } catch (ex: Exception) {
            ex.printStackTrace()
            LogUtils.e("TAG", "Error updating UI ")
        }

    }

    private fun initUI() {

        binding.dailyForecastRv.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        binding.dailyForecastRv.adapter = dailyAdapter


        binding.hourlyForecastRv.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        binding.hourlyForecastRv.adapter = hourlyAdapter

    }


    private fun degreeToDirection(degree: Double): String {

        return if (degree >= 348.75) "North" else if (degree > 326.25) "North North West"
        else if (degree > 303.75) "North West" else if (degree > 281.25) "West North West"
        else if (degree > 258.75) "West" else if (degree > 236.25) "West South West"
        else if (degree > 213.75) "South West" else if (degree > 191.25) "South South West"
        else if (degree > 168.75) "South" else if (degree > 146.25) "South South East"
        else if (degree > 123.75) "South East" else if (degree > 101.25) "East South East"
        else if (degree > 78.75) "East" else if (degree > 56.25) "East North East"
        else if (degree > 33.75) "North East" else if (degree > 11.25) "North North East"
        else if (degree >= 0) "North" else "--"

    }

}