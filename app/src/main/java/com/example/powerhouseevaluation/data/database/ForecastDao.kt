package com.example.powerhouseevaluation.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecastEntity: ForecastEntity)

    @Query("SELECT * FROM forecast_table ORDER BY id ASC")
    fun readForecast(): Flow<List<ForecastEntity>>
}