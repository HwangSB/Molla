package com.example.molla.websocket.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class EmotionAnalysisResponse(
    val userId: Long,
    val status: String,
    val result: String,
    val description: String?,
)