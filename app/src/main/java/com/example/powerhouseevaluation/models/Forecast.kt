package com.example.powerhouseevaluation.models


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday")
    val forecastday: List<ForecastdayX>
)