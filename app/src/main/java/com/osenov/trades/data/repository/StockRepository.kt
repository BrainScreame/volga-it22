package com.osenov.trades.data.repository

import com.osenov.trades.data.local.StockDao
import com.osenov.trades.data.local.room_entity.StockEntity
import com.osenov.trades.data.remote.StockRemoteDataSource
import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockUI
import javax.inject.Inject

class StockRepository @Inject constructor(
    private val stockRemoteDataSource: StockRemoteDataSource,
    private val stockDao: StockDao
) {
    suspend fun getStocks(): Result<List<StockUI>> {
        return try {
            val response = stockRemoteDataSource.fetchStocks()
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toStockUI() } ?: emptyList())
            } else {
                Result.failure(Throwable(response.errorBody().toString()))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun getStocks(query: String): Result<List<StockUI>> {
        return try {
            val response = stockRemoteDataSource.fetchStocks(query)
            if (response.isSuccessful) {
                Result.success(response.body()?.result?.map { it.toStockUI() } ?: emptyList())
            } else {
                Result.failure(Throwable(response.errorBody().toString()))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun getQuote(symbol: String): Result<Quote> {
        return try {
            //with flow get problem https://youtrack.jetbrains.com/issue/KT-27105
//            val dbResult = stockDao.getStockPrice(symbol)
//            dbResult?.let {
//                Result.success(it.quote)
//            }
            val response = stockRemoteDataSource.fetchQuote(symbol)
            if (response.isSuccessful) {
                val body = response.body()!!
                stockDao.insertStock(StockEntity(symbol, body))
                Result.success(body)
            } else {
                if (response.code() == 403) {
                    Result.failure(Throwable("You don't have access to this resource"))
                } else {
                    Result.failure(Throwable(response.errorBody().toString()))
                }
            }
        } catch (t: Throwable) {
            Result.failure(Throwable("No internet connections"))
        }
    }

}