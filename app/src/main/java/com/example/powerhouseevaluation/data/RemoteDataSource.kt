package com.example.powerhouseevaluation.data

import com.example.powerhouseevaluation.data.network.WeatherApi
import com.example.powerhouseevaluation.models.ForecastDay
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val weatherApi: WeatherApi){

    suspend fun getForecast(queries :Map<String, String>):Response<ForecastDay>{
        return weatherApi.getForecast(queries)
    }
}