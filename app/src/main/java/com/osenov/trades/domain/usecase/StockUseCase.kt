package com.osenov.trades.domain.usecase

import com.osenov.trades.data.repository.StockRepository
import javax.inject.Inject

class StockUseCase @Inject constructor(private val repository: StockRepository) {

    suspend fun getStocks() = repository.getStocks()

    suspend fun getStocks(query: String) = repository.getStocks(query)

    suspend fun getQuote(symbol: String) = repository.getQuote(symbol)

}