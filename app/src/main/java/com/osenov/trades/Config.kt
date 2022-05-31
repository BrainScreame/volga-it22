package com.osenov.trades

object Config {
    const val STOCK_URL = "https://finnhub.io/api/v1/"
    const val STOCK_SOCKET_URL = "wss://ws.finnhub.io?token=${BuildConfig.USER_ACCESS_TOKEN}"
}