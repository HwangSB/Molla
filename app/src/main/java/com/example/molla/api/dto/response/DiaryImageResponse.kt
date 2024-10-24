package com.example.molla.api.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DiaryImageResponse(
    val imageId: Long,
    val base64EncodedImage: String,
)