package com.osenov.trades.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    @SerializedName("c") val currentPrice: Double,
    @SerializedName("d") val change: Double,
    @SerializedName("dp") val percentChange: Double,
    @SerializedName("h") val highPrice: Double,
    @SerializedName("l") val lowPrice: Double,
    @SerializedName("o") val openPrice: Double,
    @SerializedName("pc") val closePrice: Double,
): Parcelable