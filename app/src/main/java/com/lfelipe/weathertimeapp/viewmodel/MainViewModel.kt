package com.lfelipe.weathertimeapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfelipe.util.TokenAuth.TOKEN
import com.lfelipe.weathertimeapp.api.ResponseApi
import com.lfelipe.weathertimeapp.api.repository.MainRepository
import com.lfelipe.weathertimeapp.model.Current
import com.lfelipe.weathertimeapp.model.CurrentWeather
import com.lfelipe.weathertimeapp.model.Token
import com.lfelipe.weathertimeapp.model.WeekForecast
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    var token: MutableLiveData<Token> = MutableLiveData()
    var currentLocalWeatherLiveData: MutableLiveData<CurrentWeather> = MutableLiveData()
    var currentWeekForecastLiveData: MutableLiveData<WeekForecast> = MutableLiveData()
    val errorMsgLiveData: MutableLiveData<String> = MutableLiveData()
    private val repository: MainRepository = MainRepository()

    fun getCurrentLocalWeather(location: String) {
        viewModelScope.launch {
            when (val response = repository.getCurrentLocalWeather(location)) {
                is ResponseApi.Success -> {
                    currentLocalWeatherLiveData.postValue(response.data as CurrentWeather?)
                }
                is ResponseApi.Error -> {
                    errorMsgLiveData.postValue(response.msg)
                }
            }
        }
    }

    fun getToken() {
    viewModelScope.launch {
        when (val response = repository.getToken()) {
            is ResponseApi.Success -> {
                token.value = response.data as Token
                TOKEN = "Bearer " + token.value?.accessToken
            }
            is ResponseApi.Error -> {
                errorMsgLiveData.value = response.msg
            }
        }
    }
}

    fun getWeekForecast(location: String) {
        viewModelScope.launch {
            when (val response = repository.getWeekForecast(location)) {
                is ResponseApi.Success -> {
                    currentWeekForecastLiveData.postValue(response.data as WeekForecast?)
                }
                is ResponseApi.Error -> {
                    errorMsgLiveData.value = response.msg
                }
            }
        }
    }


}