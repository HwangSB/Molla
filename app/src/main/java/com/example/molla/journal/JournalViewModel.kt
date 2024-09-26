package com.example.molla.journal

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.MollaApp
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.DiaryCreateRequest
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class JournalViewModel : ViewModel() {
    fun writeDiary(
        title: String,
        content: String,
        images: List<Uri>,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit
    ) {
        val diaryCreateJsonString = Gson().toJson(
            DiaryCreateRequest(
                title = title,
                content = content,
                userId = MollaApp.instance.userId.toString(),
            )
        )
        val diaryCreateRequestBody = diaryCreateJsonString.toRequestBody("application/json".toMediaTypeOrNull())

        viewModelScope.launch {
            val call = if (images.isEmpty()) {
                ApiClient.apiService.saveDiary(diaryCreateRequestBody)
            } else {
                val imageParts = mutableListOf<MultipartBody.Part>()
                val contentResolver = MollaApp.instance.contentResolver

                for (uri in images) {
                    val inputStream = contentResolver.openInputStream(uri)
                    val bytes = inputStream?.readBytes()
                    val requestFile = bytes?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val fileName = "image_${System.currentTimeMillis()}.png"
                    val part = requestFile?.let {
                        MultipartBody.Part.createFormData("images", fileName, it)
                    }
                    part?.let {
                        imageParts.add(it)
                    }
                }

                ApiClient.apiService.saveDiary(diaryCreateRequestBody, imageParts)
            }
            call.enqueue(object : Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val successResponse = response.body()
                        successResponse?.data?.let { diaryId ->
                            onSuccess(diaryId)
                        }
                        return
                    }

                    val errorBody = response.errorBody()?.toString()
                    errorBody.let {
                        try {
                            val errorResponse = Gson().fromJson(it, ErrorResponse::class.java)
                            val status = errorResponse.status
                            val errorMessage = errorResponse.message
                            val fieldErrors = errorResponse.fieldErrors

                            onError("[Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors")
                        } catch (e: Exception) {
                            onError("Json Parsing Error: ${e.message}")
                        }
                    }
                }

                override fun onFailure(
                    call: Call<StandardResponse<Long>>,
                    t: Throwable
                ) {
                    onError(t.message ?: "Network Error")
                }
            })
        }
    }
}