package com.adithyaegc.weatherapp.repository

import com.adithyaegc.weatherapp.api.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) {

    suspend fun getWeatherData(cityName: String) =
        api.getWeatherData(cityName)

}