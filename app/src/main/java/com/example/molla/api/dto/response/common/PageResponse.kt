package com.example.molla.api.dto.response.common

data class PageResponse<T>(
    val content: List<T>,
    val totalPage: Int,
    val totalElement: Long,
    val currentPage: Int,
    val last: Boolean
)