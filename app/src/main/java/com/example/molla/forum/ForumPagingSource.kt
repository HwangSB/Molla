package com.example.molla.forum

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.response.ForumListResponse

class ForumPagingSource(private val isLoggedIn: Boolean) : PagingSource<Int, ForumListResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ForumListResponse> {
        if (!isLoggedIn) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        // 페이지 번호 (default: 0)
        val pageNumber = params.key ?: 0
        val pageSize = params.loadSize
        Log.d("PagingSource", "Requesting page $pageNumber with size $pageSize")

        return try {
            // API 호출
            val response = ApiClient.apiService.getForumList(pageNumber, pageSize)
            Log.d("PagingSource", "Received response for page $pageNumber with status: ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()?.data
                if (body != null) {
                    // 다음과 이전 페이지의 키 설정 개선
                    LoadResult.Page(
                        data = body.content,
                        prevKey = if (body.currentPage == 0) null else body.currentPage - 1,
                        nextKey = if (body.last) null else body.currentPage + 1
                    )
                } else {
                    // body가 null인 경우 빈 데이터 반환
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
        // 상태를 새로고침할 때 사용할 키 결정
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            // 페이지가 맨 처음이거나 맨 끝이면 null 반환, 그렇지 않으면 페이지의 중앙으로 맞추기
            anchorPage?.nextKey?.minus(1) ?: anchorPage?.prevKey?.plus(1)
        }
    }
}
