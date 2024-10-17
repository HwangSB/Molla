package com.example.molla.api.dto.response


data class ForumListResponse(
    val postId: Long,
    val title: String,
    val content: String,
    val commentCount: Long,
    val userEmotion: String,
    val userEmotionCount: Long,
    val username: String,
    val createDate: String
)