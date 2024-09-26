package com.example.molla.api.dto.request

data class ForumCreateRequest(
    val title: String,
    val content: String,
    val userId: String
)
