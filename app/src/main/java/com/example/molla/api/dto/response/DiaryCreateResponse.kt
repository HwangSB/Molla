package com.example.molla.api.dto.response

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("status")
    val status: Int,

    @SerializedName("errorCode")
    val errorCode: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("failedErrors")
    val failedError: Map<String, Any>,
)

sealed class Data {
    data class SuccessData(val value: Int) : Data()
    data class ErrorData(val value: Error) : Data()
}

data class DiaryCreateResponse(
    @SerializedName("status")
    val status: Int,

    @SerializedName("data")
    val data: Any,
)