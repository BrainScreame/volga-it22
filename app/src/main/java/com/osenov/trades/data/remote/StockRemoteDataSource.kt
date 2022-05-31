package com.osenov.trades.data.remote

import javax.inject.Inject

class StockRemoteDataSource @Inject constructor(private val stockService: StockService) {

    suspend fun fetchStocks() = stockService.getStocks()

    suspend fun fetchStocks(query: String) = stockService.getStocks(query)

    suspend fun fetchQuote(symbol: String) = stockService.getPrice(symbol)
}
