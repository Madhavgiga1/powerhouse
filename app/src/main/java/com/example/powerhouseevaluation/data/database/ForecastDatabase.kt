package com.example.powerhouseevaluation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ForecastEntity::class], version = 1, exportSchema = false
)
@TypeConverters(ForecastTypeConverter::class)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun forecastDao():ForecastDao
}