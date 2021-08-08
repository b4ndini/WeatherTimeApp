package com.lfelipe.weathertimeapp.model

data class ForecastXX(
    val feelsLikeTemp: Int,
    val precipAccum: Double,
    val precipProb: Int,
    val symbol: String,
    val temperature: Int,
    val time: String,
    val windDir: Int,
    val windDirString: String,
    val windGust: Int,
    val windSpeed: Int
)