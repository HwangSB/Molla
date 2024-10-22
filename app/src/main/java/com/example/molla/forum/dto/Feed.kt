package com.example.molla.forum.dto

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val postId: Int,
    val title: String,
    val content: String,
    val commentCount: Int,
    val userEmotion: String,
    val emotionCount: Int,
    val writer: String,
    val timestamp: String,
)