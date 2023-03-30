package com.example.powerhouseevaluation.data.database

import androidx.room.TypeConverter
import com.example.powerhouseevaluation.models.ForecastDay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ForecastTypeConverter {
    var gson= Gson()

    @TypeConverter
    fun channeltoString(forecastDay: ForecastDay):String{
        return gson.toJson(forecastDay)
    }

    @TypeConverter
    fun stringtoChannel(data:String):ForecastDay{
        val listType=object : TypeToken<ForecastDay>() {}.type
        return gson.fromJson(data,listType)
    }
}