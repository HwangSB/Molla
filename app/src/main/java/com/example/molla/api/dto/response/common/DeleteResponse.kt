package com.example.molla.api.dto.response.common

import kotlinx.serialization.Serializable

@Serializable
data class DeleteResponse(
    val message: String,
    val id: Long
)