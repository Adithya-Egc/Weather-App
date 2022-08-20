package com.adithyaegc.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adithyaegc.weatherapp.models.WeatherResponse
import com.adithyaegc.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private val response: MutableLiveData<Response<WeatherResponse>> = MutableLiveData()
    val weatherResponse: LiveData<Response<WeatherResponse>>
        get() = response

    fun getWeatherData(cityName: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.getWeatherData(cityName)
        response.postValue(result)
    }
}