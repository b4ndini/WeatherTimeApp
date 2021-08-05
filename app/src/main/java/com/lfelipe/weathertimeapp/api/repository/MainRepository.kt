package com.lfelipe.weathertimeapp.api.repository

import com.lfelipe.weathertimeapp.api.ApiService
import com.lfelipe.weathertimeapp.api.ResponseApi
import java.lang.Exception

class MainRepository{


    suspend fun getCurrentLocalWeather(location: String?): ResponseApi {
        return try{
            val response = ApiService.weatherApi.currentWeather(location)

            if(response.isSuccessful){
                ResponseApi.Success(response.body())
            }
            else{
                ResponseApi.Error(response.code().toString())
            }
        }catch (exception: Exception){
            ResponseApi.Error("ERRO CARREGAR")
        }

    }



    suspend fun getToken() : ResponseApi{

        return try{
            val response = ApiService.weatherApi.getToken()

            if(response.isSuccessful){
                ResponseApi.Success(response.body())
            }
            else{
                ResponseApi.Error(response.code().toString())
            }
        }catch (exception: Exception){
            ResponseApi.Error("ERRO CARREGAR")
        }

    }

    suspend fun getWeekForecast(location: String?) : ResponseApi{

        return try{
            val response = ApiService.weatherApi.currentForecast(location)

            if(response.isSuccessful){
                ResponseApi.Success(response.body())
            }
            else{
                ResponseApi.Error(response.code().toString())
            }
        }catch (exception: Exception){
            ResponseApi.Error("ERRO CARREGAR")
        }

    }

    suspend fun getDailyForecast(location: String?) : ResponseApi{

        return try{
            val response = ApiService.weatherApi.dailyForecast(location)

            if(response.isSuccessful){
                ResponseApi.Success(response.body())
            }
            else{
                ResponseApi.Error(response.code().toString())
            }
        }catch (exception: Exception){
            ResponseApi.Error("ERRO CARREGAR")
        }

    }

    suspend fun getLocation(lat: String?, lon: String?) : ResponseApi{

        return try{
            val response = ApiService.locationApi.getLocation(lat, lon)

            if(response.isSuccessful){
                ResponseApi.Success(response.body())
            }
            else{
                ResponseApi.Error(response.code().toString())
            }
        }catch (exception: Exception){
            ResponseApi.Error("ERRO CARREGAR")
        }

    }

}