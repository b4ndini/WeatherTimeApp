package com.lfelipe.weathertimeapp.api

import com.lfelipe.weathertimeapp.model.Location
import com.lfelipe.weathertimeapp.util.Constants.LOCATION_API_KEY
import com.lfelipe.weathertimeapp.util.Constants.LOCATION_OUTPUT_FORMAT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("reverse.php")
    suspend fun getLocation(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("key") key: String = LOCATION_API_KEY,
        @Query("format") format: String = LOCATION_OUTPUT_FORMAT
    ): Response<Location>

}