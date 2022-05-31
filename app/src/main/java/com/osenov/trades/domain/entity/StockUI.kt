package com.osenov.trades.domain.entity

data class StockUI (
    val displaySymbol: String,
    val description: String,
    var quote: Quote? = null,
    var trade: Trade? = null,
    var quoteExceptionMessage: String? = null
)