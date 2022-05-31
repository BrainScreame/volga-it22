package com.osenov.trades.data.local.room_entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.osenov.trades.domain.entity.Quote

@Entity
data class StockEntity(
    // TODO add normal form
    // have problem with primary key auto generate
    @PrimaryKey
    val displaySymbol: String,
    @Embedded
    val quote: Quote
)