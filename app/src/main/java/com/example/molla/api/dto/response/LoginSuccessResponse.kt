package com.example.molla.api.dto.response

data class LoginSuccessResponse(
    val id: Long,
    val username: String,
    val email: String
)