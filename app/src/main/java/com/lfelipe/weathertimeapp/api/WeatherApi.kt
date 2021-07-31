package com.lfelipe.weathertimeapp.api

import com.lfelipe.util.Constants.FORECA_PASSWORD
import com.lfelipe.util.Constants.FORECA_USER
import com.lfelipe.weathertimeapp.model.Current
import com.lfelipe.weathertimeapp.model.CurrentWeather
import com.lfelipe.weathertimeapp.model.Token
import com.lfelipe.weathertimeapp.model.WeekForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherApi {

    @POST("authorize/token")
    suspend fun getToken(
        @Query("user") user : String = FORECA_USER,
        @Query("password") password : String = FORECA_PASSWORD
    ): Response<Token>

    @GET("api/v1/current/{location}")
    suspend fun currentWeather(
        @Query("location") location: String,
        @Query("lang") lang: String = "pt-br"
    ): Response<CurrentWeather>

    @GET("api/v1/forecast/daily/{location}")
    suspend fun currentForecast(
        @Query("location") location: String,
        @Query("lang") lang: String = "pt-br"
    ): Response<WeekForecast>

}