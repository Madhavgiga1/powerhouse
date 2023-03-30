package com.example.powerhouseevaluation.di

import android.content.Context
import androidx.room.Room
import com.example.powerhouseevaluation.Utils.Constants.Companion.DATABASE_NAME
import com.example.powerhouseevaluation.data.database.ForecastDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)//It indicates that a single instance of this class should be shared throughout the entire application.
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    )= Room.databaseBuilder(context,ForecastDatabase::class.java,DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideDao(database: ForecastDatabase)=database.forecastDao()
}