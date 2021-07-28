package com.lfelipe.weathertimeapp.api

import com.lfelipe.util.Constants.AUTH_LABEL
import com.lfelipe.util.Constants.AUTH_VALUE
import com.lfelipe.util.Constants.BASE_WEATHER_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    val weatherApi = getWeatherApiClient().create(WeatherApi::class.java)

    private fun getWeatherApiClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_WEATHER_URL)
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
                    .addHeader(AUTH_LABEL, AUTH_VALUE)
                    .build()
                chain.proceed(newRequest)
            }
        return interceptor.build()
    }
}