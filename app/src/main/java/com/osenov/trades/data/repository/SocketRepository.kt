package com.osenov.trades.data.repository

import android.util.Log
import com.osenov.trades.data.remote.StockRemoteDataSource
import com.osenov.trades.data.remote.WebServicesProvider
import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockSymbol
import com.osenov.trades.domain.entity.StockUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class SocketRepository @Inject constructor(
    private val stockRemoteDataSource: StockRemoteDataSource,
    private val webServicesProvider: WebServicesProvider
) {
    suspend fun getStocks(): Result<List<StockUI>> {
        return try {
            val response = stockRemoteDataSource.fetchStocks()
            if (response.isSuccessful) {
                Result.success(response.body()?.map { it.toStockUI() } ?: listOf())
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
                Result.success(response.body()?.result?.map { it.toStockUI() } ?: listOf())
            } else {
                Result.failure(Throwable(response.errorBody().toString()))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    suspend fun getQuote(symbol: String): Result<Quote> {
        return try {
            val response = stockRemoteDataSource.fetchQuote(symbol)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Throwable(response.errorBody().toString()))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    fun startSocket(tickers: List<String>) = webServicesProvider.startSocket(tickers)

    fun closeSocket() = webServicesProvider.stopSocket()

}