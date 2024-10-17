package com.example.molla.forum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.molla.api.dto.response.ForumListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import java.text.SimpleDateFormat
import java.util.Locale

class ForumListViewModel : ViewModel() {
    // `PagingData`를 직접 StateFlow로 관리
//    private val _pagingData = MutableStateFlow(createPager())
//    val pagingData: Flow<PagingData<ForumListResponse>> get() = _pagingData.value
//
//    // Pager를 생성하고 Flow를 반환
//    private fun createPager(): Flow<PagingData<ForumListResponse>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10,
//                prefetchDistance = 10,
//                enablePlaceholders = false,
//                initialLoadSize = 30
//            ),
//            pagingSourceFactory = { ForumPagingSource() }
//        ).flow.cachedIn(viewModelScope)
//    }
    val pagingData = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        pagingSourceFactory = { ForumPagingSource() }
    ).flow.flowOn(Dispatchers.Default).cachedIn(viewModelScope)

    fun getEmotionType(emotion: String?): Int {
        return when (emotion) {
            "ANGRY" -> 0
            "ANXIOUS" -> 1
            "SAD" -> 2
            "HURT" -> 3
            "HAPPY" -> 4
            else -> -1
        }
    }

    fun parseDateToMonthDay(dateString: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val output = SimpleDateFormat("MM월 dd일", Locale.getDefault())
        val date = input.parse(dateString)

        return if (date != null) {
            output.format(date)
        } else {
            ""
        }
    }
}