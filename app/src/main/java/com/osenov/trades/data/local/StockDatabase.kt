package com.osenov.trades.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.osenov.trades.data.local.StockDatabase.Companion.DB_VERSION
import com.osenov.trades.data.local.room_entity.StockEntity

@Database(entities = [StockEntity::class], version = DB_VERSION)
abstract class StockDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "stock.db"
        const val DB_VERSION = 1
    }

    abstract fun stockDao(): StockDao
}