package com.osenov.trades.ui.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osenov.trades.domain.entity.Quote
import com.osenov.trades.domain.entity.StockUI
import com.osenov.trades.domain.entity.Trade
import com.osenov.trades.domain.usecase.SocketTradeUseCase
import com.osenov.trades.domain.usecase.StockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Integer.max
import java.lang.Integer.min
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val stockUseCase: StockUseCase,
    private val socketTradeUseCase: SocketTradeUseCase
) : ViewModel() {

    companion object {
        const val DEFAULT_QUERY = ""
    }

    //Scope for controlling subrequests for getting stock prices
    private val jobQuote = Job()
    private val scopeQuote: CoroutineScope = CoroutineScope(jobQuote)

    private val _stocks = MutableStateFlow<List<StockUI>>(listOf())
    val stocks = _stocks.asStateFlow()

    private val _trades = MutableSharedFlow<TradeResult>()
    val trades = _trades.asSharedFlow()

    private val _quote = MutableSharedFlow<QuoteResult>()
    val quote = _quote.asSharedFlow()

    private val _query = MutableStateFlow(DEFAULT_QUERY)
    val query: StateFlow<String> = _query.asStateFlow()

    private val hashTrades = MutableStateFlow(HashMap<String, Int>())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            query.map(::getStocks).collect { stocks ->
                stocks?.let { _stocks.value = it }
            }
        }
    }

    fun setQuery(query: String) {
        _query.tryEmit(query)
    }

    private suspend fun getStocks(query: String): List<StockUI>? {
        if (query == DEFAULT_QUERY) {
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
        //Cancel all previous requests
        jobQuote.cancelChildren()

        //Delete previous stock positions
        removeHashTrades()

        //Add current promotions that are visible to the user
        val items = arrayListOf<String>()
        for (i in max(startPos, 0)..min(endPos, stocks.value.size - 1)) {
            items.add(_stocks.value[i].displaySymbol)
            hashTrades.value[_stocks.value[i].displaySymbol] = i
        }

        //Getting prices for visible stocks
        scopeQuote.launch(Dispatchers.IO) {
            for (i in max(startPos, 0)..min(endPos, stocks.value.size - 1)) {
                if (i < stocks.value.size && stocks.value[i].quote == null && jobQuote.isActive) {
                    val time = System.currentTimeMillis()

                    // TODO
                    val result = stockUseCase.getQuote(_stocks.value[i].displaySymbol)
                    result.onSuccess {
                        _quote.emit(QuoteResult(i, Result.success(it)))
                    }
                    result.onFailure {
                        _quote.emit(QuoteResult(i, Result.failure(it)))
                    }

                    //TODO Come up with a better way to control requests
                    delay(1000 - (System.currentTimeMillis() - time))
                }
            }
        }

        //Changing the list of stocks in a socket
        viewModelScope.launch(Dispatchers.IO) {
            socketTradeUseCase.startSocket(items).collect { trades ->
                trades.forEach { trade ->
                    val position = hashTrades.value[trade.ticker]
                    position?.let { pos ->
                        _trades.emit(TradeResult(pos, trade))
                    }
                }
            }
        }
    }

    fun closeSocket() {
        socketTradeUseCase.closeSocket()
    }

    private fun removeHashTrades() {
        val iterator = hashTrades.value.iterator()
        while (iterator.hasNext()) {
            iterator.next()
            iterator.remove()
        }
    }
}

data class QuoteResult(val position: Int, val result: Result<Quote>)
data class TradeResult(val position: Int, val trade: Trade)