package com.example.molla.forum.dto

data class Feed(
    val feedId: Int,
    val title: String,
    val content: String,
    val commentCount: Int,
    val emotionType: Int,
    val emotionCount: Int,
    val writer: String,
    val timestamp: String,
)