package com.example.molla.api.dto.request

import com.google.gson.annotations.SerializedName

data class DiaryCreateRequest(
    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("userId")
    val userId: String,
)