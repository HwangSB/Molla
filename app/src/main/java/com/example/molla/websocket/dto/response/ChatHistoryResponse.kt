package com.example.molla.websocket.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatHistoryResponse(
    val message: String,
    val isBot: Boolean,
    val createDate: String,
)