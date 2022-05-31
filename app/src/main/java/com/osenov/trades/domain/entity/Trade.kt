package com.osenov.trades.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trade(
    @SerializedName("p")
    val lastPrice: Double,

    @SerializedName("s")
    val ticker: String,

    @SerializedName("t")
    val timestamp: Long,

    @SerializedName("v")
    val volume: Double
): Parcelable