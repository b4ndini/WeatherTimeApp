package com.lfelipe.weathertimeapp.api

import com.lfelipe.weathertimeapp.model.*
import com.lfelipe.weathertimeapp.util.Constants.FORECA_PASSWORD
import com.lfelipe.weathertimeapp.util.Constants.FORECA_USER
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {

    @POST("authorize/token")
    suspend fun getToken(
        @Query("user") user : String = FORECA_USER,
        @Query("password") password : String = FORECA_PASSWORD
    ): Response<Token>

    @GET("api/v1/current/{location}")
    suspend fun currentWeather(
        @Query("location") location: String?,
        @Query("lang") lang: String = "pt-br",
        @Query("windunit") wind : String = "KMH"
    ): Response<CurrentWeather>

    @GET("api/v1/forecast/daily/{location}")
    suspend fun currentForecast(
        @Query("location") location: String?,
        @Query("lang") lang: String = "pt-br"
    ): Response<WeekForecast>

    @GET("api/v1/forecast/daily/{location}")
    suspend fun dailyForecast(
        @Query("location") location: String?,
        @Query("periods") days: Int = 14,
        @Query("lang") lang: String = "pt-br"
    ): Response<DailyForecast>

    @GET("api/v1/location/search/{query}")
    suspend fun searchLocations(
        @Path("query") query: String,
        @Query("lang") lang: String = "pt-br"
    ): Response<Locations>

    @GET("api/v1/forecast/hourly/{location}")
    suspend fun hourlyForecast(
        @Query("location") location: String?,
        @Query("dataset") dataset: String = "full"
    ): Response<Hourly>


}