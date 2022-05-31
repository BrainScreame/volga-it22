package com.osenov.trades.data.remote

import android.util.Log
import com.google.gson.Gson
import com.osenov.trades.domain.entity.ResponseSocket
import com.osenov.trades.domain.entity.Trade
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class SocketListener @Inject constructor() : WebSocketListener() {

    var socketFlow = MutableSharedFlow<List<Trade>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.i("MySocket", "Socket response: $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        updatePriceInDatabase(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.i("MySocket", "onClosing with code: $code")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.i("MySocket", "onClosed with code: $code")
    }


    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.i("MySocket", "Throwable: $t")
        Log.i("MySocket", "Throwable: $response")
    }

    private fun updatePriceInDatabase(string: String) {
        val response = Gson().fromJson(string, ResponseSocket::class.java)

        response.data?.let {
            GlobalScope.launch {
                socketFlow.emit(it)
            }
        }
    }
}