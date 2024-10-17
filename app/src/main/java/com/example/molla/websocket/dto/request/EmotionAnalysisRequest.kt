package com.example.molla.websocket.dto.request

data class EmotionAnalysisRequest(
    val userId: Long,
    val targetId: Long,
    val content: String,
    val domain: String,
)