package com.example.molla.api.dto.request

data class CommentSaveRequest(
    val content: String,
    val userId: Long,
    val postId: Long
)