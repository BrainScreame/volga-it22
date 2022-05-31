package com.osenov.trades.ui.stock

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockSymbol
import com.osenov.trades.domain.entity.StockUI
import com.osenov.trades.domain.entity.Trade
import com.osenov.trades.domain.usecase.StockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Integer.max
import java.lang.Integer.min
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val stockUseCase: StockUseCase) : ViewModel() {

    companion object {
        private const val DEFAULT_QUERY = ""
    }

    var scopeChildren : CoroutineScope? = null

    private val _stocks = MutableStateFlow<List<StockUI>>(listOf())
    val stocks = _stocks.asStateFlow()

    private val _trades = MutableStateFlow<List<Trade>>(listOf())
    val trades = _trades.asStateFlow()

    private val _quote =
        MutableSharedFlow<QuoteResult>()
    val quote = _quote.asSharedFlow()

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        viewModelScope.launch {
            query.map(::getStocks).collect { stocks ->
                stocks?.let { _stocks.value = it }
            }
        }
    }

    fun setQuery(query: String) {
        //viewModelScope.coroutineContext.cancelChildren()
        _query.tryEmit(query)
    }

    private suspend fun getStocks(query: String): List<StockUI>? {
        if(query == DEFAULT_QUERY) {
            return stockUseCase.getStocks().getOrNull()
        }
        return stockUseCase.getStocks(query).getOrNull()
    }

    fun getStocks() {
        viewModelScope.launch {
            val result = stockUseCase.getStocks()
            result.onSuccess {
                _stocks.value = it
            }
        }
    }

    fun updatePrices(startPos: Int, endPos: Int) {
        scopeChildren?.cancel()
        scopeChildren = null
        scopeChildren = CoroutineScope(Job())
        //viewModelScope.coroutineContext.cancelChildren()
        val items = arrayListOf<String>()
        for (i in max(startPos, 0)..min(endPos, stocks.value.size - 1)) {
            items.add(_stocks.value[i].displaySymbol)
        }

        scopeChildren?.launch {
            for (i in max(startPos, 0)..min(endPos, stocks.value.size - 1)) {
                if (_stocks.value[i].quote == null) {
                    val time = System.currentTimeMillis()
                    val result = stockUseCase.executeQuote(_stocks.value[i].displaySymbol)
                    result.onSuccess {
                        _quote.emit(QuoteResult(i, Result.success(it)))
                    }
                    result.onFailure {
                        _quote.emit(QuoteResult(i, Result.failure(it)))
                    }
                    delay(1000 - (System.currentTimeMillis() - time))
                }
            }
        }

        viewModelScope.launch {
            stockUseCase.startSocket(items).collect {
                _trades.value = it
            }
        }

    }
}

data class QuoteResult(val position: Int, val result: Result<Quote>)