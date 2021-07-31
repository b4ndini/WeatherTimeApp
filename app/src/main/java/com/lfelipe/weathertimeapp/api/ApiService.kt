package com.lfelipe.weathertimeapp.api

import android.content.Context
import android.content.SharedPreferences
import com.lfelipe.util.Constants.AUTH_LABEL
import com.lfelipe.util.Constants.BASE_WEATHER_URL
import com.lfelipe.util.TokenAuth.TOKEN
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.coroutineContext

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
                    .addHeader(AUTH_LABEL, TOKEN)
                    .build()
                chain.proceed(newRequest)
            }
        return interceptor.build()
    }
}