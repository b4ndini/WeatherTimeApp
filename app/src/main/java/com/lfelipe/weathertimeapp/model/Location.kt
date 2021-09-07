package com.lfelipe.weathertimeapp.model

import com.google.gson.annotations.SerializedName

data class Location(
    val address: Address,
    val boundingbox: List<String>,
    @SerializedName("display_name")
    val displayName: String,
    val lat: String,
    val licence: String,
    val lon: String,
  /*  val osm_id: String,
    val osm_type: String,
    val place_id: String*/
)