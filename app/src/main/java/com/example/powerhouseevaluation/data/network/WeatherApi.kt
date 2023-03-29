package com.example.powerhouseevaluation.data.network

import com.example.powerhouseevaluation.models.ForecastDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherApi {

    @GET(value="/v1/forecast.json")
    suspend fun getForecast(@QueryMap forecastqueries: Map<String, String>) :Response<ForecastDay>
}