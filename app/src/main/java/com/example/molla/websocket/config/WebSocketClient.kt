package com.example.molla.websocket.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.example.molla.BuildConfig
import com.example.molla.MollaApp
import com.example.molla.websocket.dto.response.ChatResponse
import com.example.molla.websocket.dto.response.EmotionAnalysisResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

enum class WebSocketPath(val str: String) {
    EMOTION("emotion-analysis"),
    CHAT("chat")
}

object WebSocketManager {
    private const val BASE_URL = "ws://${BuildConfig.SERVER_IP}:${BuildConfig.SERVER_PORT}"

    fun getWebSocketUrl(endpoint: String): String {
        return "$BASE_URL/$endpoint"
    }

    fun createClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}

class EmotionWebSocketListener(private val onMessageReceived: ((EmotionAnalysisResponse) -> Unit)?) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        println("Connection opened")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Received message: $text")
        val emotionAnalysisResponse = Gson().fromJson(text, EmotionAnalysisResponse::class.java)
        onMessageReceived?.let { it(emotionAnalysisResponse) }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("Closing WebSocket: $code / $reason")
        webSocket.close(1000, null)
    }
}

class ChatWebSocketListener(private val onMessageReceived: ((ChatResponse) -> Unit)?) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        println("Connection opened")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Received message: $text")
        val chatResponse = Gson().fromJson(text, ChatResponse::class.java)
        onMessageReceived?.let { it(chatResponse) }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("Closing WebSocket: $code / $reason")
        webSocket.close(1000, null)
    }
}

@Composable
fun WebSocketClient(
    path: WebSocketPath,
    onWebSocketCreated: (WebSocket) -> Unit,
    onEmotionAnalysisMessageReceived: ((EmotionAnalysisResponse) -> Unit)? = null,
    onChatMessageReceived: ((ChatResponse) -> Unit)? = null
) {
    val userId = MollaApp.instance.userId ?: -1
    val endpoint = "ws/${path.str}?client=user&id=$userId"
    val wsUrl = WebSocketManager.getWebSocketUrl(endpoint)
    val client = WebSocketManager.createClient()
    val listener = when (path) {
        WebSocketPath.EMOTION -> EmotionWebSocketListener(onEmotionAnalysisMessageReceived)
        WebSocketPath.CHAT -> ChatWebSocketListener(onChatMessageReceived)
    }

    DisposableEffect(Unit) {
        val request = Request.Builder().url(wsUrl).build()
        val webSocket = client.newWebSocket(request, listener)
        onWebSocketCreated(webSocket)
        onDispose {
            webSocket.close(1000, "Closing connection")
            client.dispatcher.executorService.shutdown()
        }
    }
}