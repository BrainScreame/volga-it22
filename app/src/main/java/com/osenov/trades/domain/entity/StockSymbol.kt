package com.osenov.trades.domain.entity

import com.google.gson.annotations.SerializedName

data class StockSymbol(
    val currency: String,
    val description: String,
    val displaySymbol: String,
    @SerializedName("figi") val identifierFIGI: String,
    @SerializedName("isin") val identifierISIN: String? = null,
    @SerializedName("mic") val identifierMIC: String,
    val shareClassFIGI: String,
    @SerializedName("symbol") val stockSymbol: String,
    @SerializedName("symbol2") val alternativeStockSymbol: String,
    val type: String,
    var quote: Quote? = null,
    var trade: Trade? = null
) {
    fun toStockUI() = StockUI(displaySymbol, description)
}