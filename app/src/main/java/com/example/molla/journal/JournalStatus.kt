package com.example.molla.journal

sealed class DeleteStatus {
    object Idle : DeleteStatus()
    object Loading : DeleteStatus()
    data class Success(val diaryId: Long) : DeleteStatus()
    data class Error(val message: String) : DeleteStatus()
}