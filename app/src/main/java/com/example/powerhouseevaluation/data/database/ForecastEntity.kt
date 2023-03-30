package com.example.powerhouseevaluation.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomMasterTable.TABLE_NAME
import com.example.powerhouseevaluation.Utils.Constants.Companion.FORECAST_TABLE
import com.example.powerhouseevaluation.models.ForecastDay


@Entity(tableName = FORECAST_TABLE)
class ForecastEntity(var forecastDay: ForecastDay) {
    @PrimaryKey(autoGenerate = false)
    var id:Int = 0
}