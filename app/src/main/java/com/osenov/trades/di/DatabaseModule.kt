package com.osenov.trades.di

import android.content.Context
import androidx.room.Room
import com.osenov.trades.data.local.StockDao
import com.osenov.trades.data.local.StockDatabase
import com.osenov.trades.data.local.StockDatabase.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): StockDatabase {
        return Room.databaseBuilder(
            appContext,
            StockDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(stockDatabase: StockDatabase): StockDao {
        return stockDatabase.stockDao()
    }
}