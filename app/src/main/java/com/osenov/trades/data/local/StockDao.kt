package com.osenov.trades.data.local

import androidx.room.*
import com.osenov.trades.data.local.room_entity.StockEntity

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stockEntity: StockEntity)

    @Query("SELECT * FROM StockEntity WHERE :name = displaySymbol")
    suspend fun getStockPrice(name: String) : StockEntity?
}