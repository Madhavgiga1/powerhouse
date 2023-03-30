package com.example.powerhouseevaluation.data

import com.example.powerhouseevaluation.data.database.ForecastDao
import com.example.powerhouseevaluation.data.database.ForecastEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val forecastDao: ForecastDao) {

    fun readDatabase(): Flow<List<ForecastEntity>> {
        return forecastDao.readForecast()
    }
    suspend fun insertChannels(forecastEntity: ForecastEntity){
        forecastDao.insertForecast(forecastEntity)
    }
}