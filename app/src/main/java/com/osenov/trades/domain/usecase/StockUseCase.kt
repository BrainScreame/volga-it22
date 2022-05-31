package com.osenov.trades.domain.usecase

import com.osenov.trades.data.repository.SocketRepository
import com.osenov.trades.domain.entity.Trade
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockUseCase @Inject constructor(private val repository: SocketRepository) {

    suspend fun getStocks() = repository.getStocks()

    suspend fun getStocks(query: String) = repository.getStocks(query)

    suspend fun executeQuote(symbol: String) = repository.getQuote(symbol)

    fun startSocket(tickers: List<String>) = repository.startSocket(tickers)

    fun closeSocket() = repository.closeSocket()

}