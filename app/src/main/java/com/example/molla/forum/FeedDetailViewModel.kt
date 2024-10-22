package com.example.molla.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.response.PostDetail
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.example.molla.forum.dto.Feed
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
}