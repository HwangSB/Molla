package com.example.molla.journal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.molla.api.config.ApiClient
import com.example.molla.api.config.ApiService
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.api.dto.response.common.DeleteResponse
import kotlinx.coroutines.flow.Flow

class JournalRepository {
    fun getJournalPagingFlow(): Flow<PagingData<DiaryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = ApiService.PAGE_SIZE.JOURNAL,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { JournalPagingSource() }
        ).flow
    }

    suspend fun deleteJournal(diaryId: Long): Result<DeleteResponse> {
        return try {
            val response = ApiClient.apiService.deleteDiary(diaryId)
            if (response.isSuccessful) {
                Result.success(response.body()?.data!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        @Volatile
        private var instance: JournalRepository? = null

        fun getInstance(): JournalRepository {
            return instance ?: synchronized(this) {
                instance ?: JournalRepository().also { instance = it }
            }
        }
    }
}