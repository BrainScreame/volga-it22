package com.osenov.trades.data.repository

import com.osenov.trades.data.remote.socket.WebServicesProvider
import javax.inject.Inject


class SocketTradeRepository @Inject constructor(
    private val webServicesProvider: WebServicesProvider
) {

    fun startSocket(tickers: List<String>) = webServicesProvider.startSocket(tickers)

    fun closeSocket() = webServicesProvider.stopSocket()
}