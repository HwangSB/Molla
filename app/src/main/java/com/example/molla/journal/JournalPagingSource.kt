package com.example.molla.journal

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.molla.MollaApp
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.response.DiaryResponse
import okio.IOException
import retrofit2.HttpException

class JournalPagingSource : PagingSource<Int, DiaryResponse>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DiaryResponse> {
        return try {
            val pageNumber = params.key ?: 0
            val userId = MollaApp.instance.userId ?: return LoadResult.Error(Exception("User ID is null"))
            val response = ApiClient.apiService.listDiaries(userId, pageNumber, params.loadSize)
            if (!response.isSuccessful) {
                return LoadResult.Error(Exception("Load Journal Error: ${response.code()}"))
            }

            val journals = response.body()?.data?.content
            LoadResult.Page(
                data = journals ?: emptyList(),
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if (journals.isNullOrEmpty()) null else { pageNumber + 1 }
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DiaryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}