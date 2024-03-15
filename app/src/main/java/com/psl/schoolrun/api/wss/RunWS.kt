package com.psl.schoolrun.api.wss

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class RunWS {

    private val client: OkHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private val gson = Gson()

    fun connect(url: String, listener: WebSocketListener) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: Any) {
        val json = gson.toJson(message)
        webSocket?.send(json)
    }

    fun close() {
        webSocket?.close(1000, "Service destroyed")
    }

}