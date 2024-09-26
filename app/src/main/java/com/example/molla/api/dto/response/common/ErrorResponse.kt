package com.example.molla.api.dto.response.common

data class ErrorResponse(
    val status: Int,
    val errorCode: String,
    val message: String,
    val fieldErrors: Map<String, String>
)