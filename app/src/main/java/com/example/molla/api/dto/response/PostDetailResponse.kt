package com.example.molla.api.dto.response

import java.time.LocalDateTime

data class PostDetail(
    val title: String,
    val content: String,
    val postEmotion: String,
    val userEmotion: String,
    val userEmotionCount: Long,
    val username: String,
    val createDate: LocalDateTime,
    val comments: List<Comment>
)

data class Comment(
    val id: Long,
    val content: String,
    val username: String,
    val createdDate: String
)