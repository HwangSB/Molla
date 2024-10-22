package com.example.molla.websocket.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val userId: Long,
    val status: String,
    val content: String,
    val description: String,
)