package com.example.molla.api.dto.response.common

data class StandardResponse<T> (
    val status: Int,
    val data: T
)