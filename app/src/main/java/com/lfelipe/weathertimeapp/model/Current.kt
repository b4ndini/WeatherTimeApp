package com.lfelipe.weathertimeapp.model

data class Current(
    val cloudiness: Int,
    val dewPoint: Int,
    val feelsLikeTemp: Int,
    val precipProb: Int,
    val precipRate: Double,
    val pressure: Double,
    val relHumidity: Int,
    val symbol: String,
    val symbolPhrase: String,
    val temperature: Int,
    val thunderProb: Int,
    val time: String,
    val uvIndex: Int,
    val visibility: Int,
    val windDirString: String,
    val windGust: Int,
    val windSpeed: Int
)