package com.example.molla.websocket.config

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.example.molla.BuildConfig
import com.example.molla.MollaApp
import com.example.molla.websocket.dto.response.EmotionAnalysisResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

object WebSocketManager {
    private const val BASE_URL = "ws://${BuildConfig.SERVER_IP}:${BuildConfig.SERVER_PORT}/ws/emotion-analysis?client=user&id="

    fun getWebSocketUrl(userId: Long): String {
        return "$BASE_URL$userId"
    }

    fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun createRequest(url: String): Request {
        return Request.Builder().url(url).build()
    }
}

class EmotionWebSocketListener(private val onMessageReceived: (EmotionAnalysisResponse) -> Unit) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        println("Connection opened")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Received message: $text")
        val emotionAnalysisResponse = Gson().fromJson(text, EmotionAnalysisResponse::class.java)
        onMessageReceived(emotionAnalysisResponse)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("Closing WebSocket: $code / $reason")
        webSocket.close(1000, null)
    }
}

@Composable
fun WebSocketClient(onWebSocketCreated: (WebSocket) -> Unit, onMessageReceived: (EmotionAnalysisResponse) -> Unit) {
    val wsUrl = WebSocketManager.getWebSocketUrl(MollaApp.instance.userId ?: -1)
    val client = WebSocketManager.createClient()
    val listener = EmotionWebSocketListener(onMessageReceived)

    DisposableEffect(Unit) {
        val webSocket = client.newWebSocket(WebSocketManager.createRequest(wsUrl), listener)
        onWebSocketCreated(webSocket)
        onDispose {
            webSocket.close(1000, "Closing connection")
            client.dispatcher.executorService.shutdown()
        }
    }
}