package com.example.molla.websocket.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val userId: Long,
    val message: String,
    val isBot: Boolean,
)