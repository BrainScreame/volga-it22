package com.osenov.trades.data.remote.socket

import android.util.Log
import com.osenov.trades.Config.STOCK_SOCKET_URL
import com.osenov.trades.data.remote.socket.SocketListener.Companion.SOCKET_STOCK_TAG
import com.osenov.trades.domain.entity.Trade
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Inject

class WebServicesProvider @Inject constructor(private val listener: SocketListener) {

    private val socketClient = OkHttpClient.Builder().build()

    private val webSocket: WebSocket = socketClient.newWebSocket(
        Request.Builder().url(STOCK_SOCKET_URL).build(), listener
    )

    private val usingTickers = HashMap<String, Int>()


    fun startSocket(tickers: List<String>): Flow<List<Trade>> =
        with(listener) {
            subscribe(tickers)
            this.socketFlow
        }


    private fun subscribe(tickers: List<String>) {
        for (ticker in tickers) {
            if (usingTickers[ticker] == null) {
                usingTickers[ticker] = -1
            } else {
                usingTickers[ticker] = usingTickers[ticker]!! - 1
            }
        }

        val iterator = usingTickers.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            when (usingTickers[item.key]) {
                1 -> {
                    webSocket.send("{\"type\":\"unsubscribe\",\"symbol\":\"${item.key}\"}")
                    //Log.i(SOCKET_STOCK_TAG, "unsubscribe: ${item.key}")
                    iterator.remove()
                }
                -1 -> {
                    webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"${item.key}\"}")
                    //Log.i(SOCKET_STOCK_TAG, "subscribe: ${item.key}")
                    usingTickers[item.key] = 1
                }
                else -> {
                    usingTickers[item.key] = 1
                }
            }
        }
    }

    fun stopSocket() {
        Log.i(SOCKET_STOCK_TAG, "close socket:")
        webSocket.close(NORMAL_CLOSURE_STATUS, "app closed socket")
    }

    companion object {
        const val NORMAL_CLOSURE_STATUS = 1000
    }
}