package com.example.molla.counsel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.MollaApp
import com.example.molla.api.config.ApiClient
import com.example.molla.websocket.dto.response.ChatHistoryResponse
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselViewModel : ViewModel() {
    fun getChatHistory(
        onSuccess: (List<ChatHistoryResponse>) -> Unit,
        onError: (String) -> Unit,
    ) {
        MollaApp.instance.userId?.let {
            val call = ApiClient.apiService.getChatHistory(it)
            viewModelScope.launch {
                call.enqueue(object : Callback<StandardResponse<List<ChatHistoryResponse>>> {
                    override fun onResponse(
                        call: Call<StandardResponse<List<ChatHistoryResponse>>>,
                        response: Response<StandardResponse<List<ChatHistoryResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.data?.let { chatHistory ->
                                onSuccess(chatHistory)
                            } ?: onError("No data in response")
                            return
                        }

                        handleErrorResponse(response, onError)
                    }

                    override fun onFailure(
                        call: Call<StandardResponse<List<ChatHistoryResponse>>>,
                        t: Throwable
                    ) {
                        onError(t.message ?: "Network Error")
                    }
                })
            }
        }
    }

    private fun<T> handleErrorResponse(response: Response<StandardResponse<T>>, onError: (String) -> Unit) {
        val errorBody = response.errorBody()?.toString()
        errorBody?.let {
            try {
                val standardErrorResponse = Gson().fromJson(it, StandardResponse::class.java)
                val errorResponse = Gson().fromJson(
                    Gson().toJson(standardErrorResponse.data),
                    ErrorResponse::class.java
                )

                val status = errorResponse.status
                val errorMessage = errorResponse.message
                val fieldErrors = errorResponse.fieldErrors

                onError("[Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors")
            } catch (e: Exception) {
                onError("Json Parsing Error: ${e.message}")
            }
        } ?: run {
            onError("Unknown Error")
        }
    }
}