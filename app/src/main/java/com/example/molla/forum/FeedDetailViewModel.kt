package com.example.molla.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.MollaApp
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.CommentSaveRequest
import com.example.molla.api.dto.response.Comment
import com.example.molla.api.dto.response.PostDetail
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

class FeedDetailViewModel : ViewModel() {
    fun fetchPostDetail(postId: Long, onSuccess: (PostDetail) -> Unit, onError: (String) -> Unit) {
        val retrofit = ApiClient.apiService.getPostDetail(postId)

        viewModelScope.launch {
            retrofit.enqueue(object : Callback<StandardResponse<PostDetail>> {
                override fun onResponse(
                    call: Call<StandardResponse<PostDetail>>,
                    response: Response<StandardResponse<PostDetail>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val successResponse = response.body()
                        successResponse?.data?.let { postDetailData ->
                            onSuccess(postDetailData)
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorBody?.let {
                            try {
                                val gson = Gson()
                                val errorResponse = gson.fromJson(it, ErrorResponse::class.java)
                                val status = errorResponse.status
                                val errorMessage = errorResponse.message
                                val fieldErrors = errorResponse.fieldErrors

                                onError("[Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors")
                            } catch (e: Exception) {
                                onError("Json Parsing Error: ${e.message}")
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<StandardResponse<PostDetail>>,
                    t: Throwable
                ) {
                    onError(t.message ?: "Network error")
                }
            })
        }
    }

    fun saveComment(content: String, postId: Long, userId: Long, createDate: String, onSuccess: (Comment) -> Unit, onError: (String) -> Unit) {
        val request = CommentSaveRequest(
            content = content,
            postId = postId,
            userId = userId
        )

        val retrofit = ApiClient.apiService.saveComment(request)

        viewModelScope.launch {
            retrofit.enqueue(object : Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val successResponse = response.body()
                        val commentId = successResponse?.data
                        if (commentId != null) {
                            val newComment = Comment(
                                id = commentId,
                                content = content,
                                username = MollaApp.instance.username!!,
                                createdDate = createDate
                            )
                            onSuccess(newComment)
                        } else {
                            onError("댓글 ID를 가져올 수 없습니다.")
                        }
                    } else {
                        val errorBody = response.errorBody()?.toString()
                        errorBody.let {
                            try {
                                val gson = Gson()
                                val errorResponse = gson.fromJson(it, ErrorResponse::class.java)
                                val status = errorResponse.status
                                val errorMessage = errorResponse.message
                                val fieldErrors = errorResponse.fieldErrors

                                onError("[Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors")
                            } catch (e: Exception) {
                                onError("Json Parsing Error: ${e.message}")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<StandardResponse<Long>>, t: Throwable) {
                    onError(t.message ?: "Network Error")
                }
            })
        }
    }

    fun parseDateToMonthDay(dateString: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val output = SimpleDateFormat("M월 dd일", Locale.getDefault())
        val date = input.parse(dateString)

        return if (date != null) {
            output.format(date)
        } else {
            ""
        }
    }
}