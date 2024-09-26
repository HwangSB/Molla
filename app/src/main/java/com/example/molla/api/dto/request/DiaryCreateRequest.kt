package com.example.molla.api.dto.request

data class DiaryCreateRequest(
    val title: String,
    val content: String,
    val userId: String,
)