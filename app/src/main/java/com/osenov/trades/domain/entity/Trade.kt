package com.osenov.trades.domain.entity

import com.google.gson.annotations.SerializedName

data class Trade(
    @SerializedName("p")
    val lastPrice: Double,

    @SerializedName("s")
    val ticker: String,

    @SerializedName("t")
    val timestamp: Long,

    @SerializedName("v")
    val volume: Double
)