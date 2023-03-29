package com.example.powerhouseevaluation.models


import com.google.gson.annotations.SerializedName

data class ForecastDay(
    @SerializedName("current")
    val current: Current,
    @SerializedName("forecast")
    val forecast: Forecast,
    @SerializedName("location")
    val location: Location
)