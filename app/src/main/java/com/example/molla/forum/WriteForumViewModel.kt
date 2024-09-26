package com.example.molla.forum

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.ForumCreateRequest
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteForumViewModel : ViewModel() {

    fun saveForum(title: String, content: String, userId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

        val forumCreateRequest = ForumCreateRequest(title, content, userId)
        val retrofit = ApiClient.apiService.saveForum(forumCreateRequest)

        viewModelScope.launch {
            retrofit.enqueue(object: Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val successResponse = response.body()
                        successResponse?.data?.let { it ->
                            Log.d("게시글", "게시글 작성 성공 : $it")
                            onSuccess()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("회원가입", "Raw error body: $errorBody")
                        errorBody.let {
                            try {
                                val gson = Gson()
                                val standardErrorResponse = gson.fromJson(it, StandardResponse::class.java)
                                val errorData = gson.fromJson(gson.toJson(standardErrorResponse.data), ErrorResponse::class.java)

                                val status = errorData.status
                                val errorMessage = errorData.message
                                val fieldErrors = errorData.fieldErrors.ifEmpty { "No field errors" }

                                onError("[Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors")
                                Log.e("게시글",
                                    "게시글 작성 실패: [Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors"
                                )
                            } catch (e: Exception) {
                                onError("Json Parsing Error: ${e.message}")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<StandardResponse<Long>>, t: Throwable) {
                    Log.e("게시글", "게시글 작성 실패: ${t.message ?: "Network Error"}")
                    onError(t.message ?: " Network Error")
                }
            })
        }
    }

    fun validateInput(title: String, content: String): String? {
        if (title.isBlank()) return "제목을 입력해주세요."
        if (content.isBlank()) return "내용을 입력해주세요."
        return null
    }
}