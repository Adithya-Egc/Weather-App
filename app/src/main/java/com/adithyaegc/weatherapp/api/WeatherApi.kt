package com.adithyaegc.weatherapp.api

import com.adithyaegc.weatherapp.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    /**
     * before using this application paste your api key inside getWeatherData function
     */

    @GET("weather?appid= PASTE YOUR API KEY HERE &units=metric")
    suspend fun getWeatherData(
        @Query("q") cityName: String
    ): Response<WeatherResponse>
}