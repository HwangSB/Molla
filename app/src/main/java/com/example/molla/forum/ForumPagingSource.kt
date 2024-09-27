package com.example.molla.forum

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.response.ForumListResponse

class ForumPagingSource(private val isLoggedIn: Boolean) : PagingSource<Int, ForumListResponse>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ForumListResponse> {
        if (!isLoggedIn) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        val pageNumber = params.key ?: 0
        val pageSize = params.loadSize
        Log.d("PagingSource", "Requesting page $pageNumber with size $pageSize")

        return try {
            val response = ApiClient.apiService.getForumList(pageNumber, pageSize)
            Log.d("PagingSource", "Received response for page $pageNumber with status: ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()?.data
                if (body != null) {
                    LoadResult.Page(
                        data = body.content,
                        prevKey = if (pageNumber == 0) null else pageNumber - 1,
                        nextKey = if (body.last) null else pageNumber + 1
                    )
                } else {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
            } else {
                LoadResult.Error(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ForumListResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}