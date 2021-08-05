package com.lfelipe.weathertimeapp.model

data class ForecastX(
    val date: String,
    val maxTemp: Int,
    val maxWindSpeed: Int,
    val minTemp: Int,
    val precipAccum: Double,
    val symbol: String,
    val windDir: Int
)