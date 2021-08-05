package com.lfelipe.weathertimeapp.api

import com.lfelipe.weathertimeapp.util.Constants.AUTH_LABEL
import com.lfelipe.weathertimeapp.util.Constants.LOCATION_BASE_URL
import com.lfelipe.weathertimeapp.util.Constants.WEATHER_BASE_URL
import com.lfelipe.weathertimeapp.util.TokenAuth.TOKEN
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    val weatherApi = getWeatherApiClient().create(WeatherApi::class.java)
    val locationApi = getLocationApiClient().create(LocationApi::class.java)

    private fun getLocationApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getWeatherApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(getInterceptorClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getInterceptorClient(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        val interceptor = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader(
                        AUTH_LABEL,
                        TOKEN
                    )
                    .build()
                chain.proceed(newRequest)
            }
        return interceptor.build()
    }
}