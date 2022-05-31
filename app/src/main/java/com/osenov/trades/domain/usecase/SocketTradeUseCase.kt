package com.osenov.trades.domain.usecase

import com.osenov.trades.data.repository.SocketTradeRepository
import javax.inject.Inject

class SocketTradeUseCase @Inject constructor(private val repository: SocketTradeRepository) {

    fun startSocket(tickers: List<String>) = repository.startSocket(tickers)

    fun closeSocket() = repository.closeSocket()
}