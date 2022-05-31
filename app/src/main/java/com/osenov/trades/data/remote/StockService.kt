package com.osenov.trades.data.remote

import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockSearchResponse
import com.osenov.trades.domain.entity.StockSymbol
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StockService {

    @GET("stock/symbol?exchange=US")
    suspend fun getStocks(): Response<List<StockSymbol>>

    @GET("search")
    suspend fun getStocks(
        @Query("q") symbol: String,
        @Query("exchange") exchange: String = "US"
    ): Response<StockSearchResponse>

    @GET("quote")
    suspend fun getPrice(@Query("symbol") symbol: String): Response<Quote>

}