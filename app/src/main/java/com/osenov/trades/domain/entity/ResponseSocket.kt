package com.osenov.trades.domain.entity

import com.google.gson.annotations.SerializedName

data class ResponseSocket(
    @SerializedName("data")
    val data : List<Trade>? = null
)
