package com.example.molla.api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DiaryImageResponse(
    val imageId: Long,
    val base64EncodedImage: String,
)

@Serializable
data class DiaryResponse(
    val diaryId: Long,
    val title: String,
    val content: String,
    val diaryEmotion: String?,
    val createDate: String,
    val images: List<DiaryImageResponse>,
)