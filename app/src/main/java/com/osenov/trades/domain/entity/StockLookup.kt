package com.osenov.trades.domain.entity

import com.google.gson.annotations.SerializedName

data class StockSearchResponse(val count: Int, val result: List<StockLookup>)

data class StockLookup(
    val description: String,
    val displaySymbol: String,
    @SerializedName("symbol") val stockSymbol: String,
    val type: String
) {
    fun toStockUI() = StockUI(displaySymbol, description)
}
