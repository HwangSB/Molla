package com.example.molla.websocket.dto.response

data class EmotionAnalysisResponse(
    val userId: Long,
    val status: String,
    val result: String,
    val description: String?,
)