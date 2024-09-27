package com.example.molla.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.MollaApp
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.DiaryCreateRequest
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.PageResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalViewModel : ViewModel() {
    fun listDiaries(
        pageNumber: Int,
        onSuccess: (List<DiaryResponse>) -> Unit,
        onError: (String) -> Unit,
    ) {
        MollaApp.instance.userId?.let {
            val call = ApiClient.apiService.listDiaries(it, pageNumber, 20)
            viewModelScope.launch {
                call.enqueue(object : Callback<StandardResponse<PageResponse<DiaryResponse>>> {
                    override fun onResponse(
                        call: Call<StandardResponse<PageResponse<DiaryResponse>>>,
                        response: Response<StandardResponse<PageResponse<DiaryResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.data?.content?.let { diaries ->
                                onSuccess(diaries)
                            } ?: onError("No data in response")
                            return
                        }

                        handleErrorResponse(response, onError)
                    }

                    override fun onFailure(
                        call: Call<StandardResponse<PageResponse<DiaryResponse>>>,
                        t: Throwable
                    ) {
                        onError(t.message ?: "Network Error")
                    }
                })
            }
        }
    }

    fun writeDiary(
        title: String,
        content: String,
        images: List<Pair<String, ByteArray>>,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit,
    ) {
        val diaryCreateRequestBody = Gson().toJson(
            DiaryCreateRequest(
                title = title,
                content = content,
                userId = MollaApp.instance.userId.toString(),
            )
        ).toRequestBody("application/json".toMediaTypeOrNull())

        val call = if (images.isEmpty()) {
            ApiClient.apiService.saveDiary(diaryCreateRequestBody)
        } else {
            val imageParts = getImageParts(images)
            ApiClient.apiService.saveDiary(diaryCreateRequestBody, imageParts)
        }

        viewModelScope.launch {
            call.enqueue(object : Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { diaryId ->
                            onSuccess(diaryId)
                        } ?: onError("No data in response")
                        return
                    }

                    handleErrorResponse(response, onError)
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

    fun updateDiary(
        diaryId: Long,
        title: String,
        content: String,
        updateImages: List<Pair<String, ByteArray>>,
        deleteImageIds: List<Long>,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit,
    ) {
        val diaryUpdateRequestBody = Gson().toJson(
            DiaryCreateRequest(
                title = title,
                content = content,
                userId = MollaApp.instance.userId.toString(),
            )
        ).toRequestBody("application/json".toMediaTypeOrNull())

        val deleteImageIdsRequestBody = Gson().toJson(deleteImageIds)
            .toRequestBody("application/json".toMediaTypeOrNull())

        val updateImageParts = getImageParts(updateImages)
        val call = ApiClient.apiService.updateDiary(
            diaryId,
            diaryUpdateRequestBody,
            updateImageParts,
            deleteImageIdsRequestBody
        )

        viewModelScope.launch {
            call.enqueue(object : Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { diaryId ->
                            onSuccess(diaryId)
                        } ?: onError("No data in response")
                        return
                    }

                    handleErrorResponse(response, onError)
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

    fun deleteDiary(
        diaryId: Long,
        onSuccess: (Long) -> Unit,
        onError: (String) -> Unit,
    ) {
        val call = ApiClient.apiService.deleteDiary(diaryId)
        viewModelScope.launch {
            call.enqueue(object : Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { diaryId ->
                            onSuccess(diaryId)
                        } ?: onError("No data in response")
                        return
                    }

                    handleErrorResponse(response, onError)
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

    private fun getImageParts(images: List<Pair<String, ByteArray>>): List<MultipartBody.Part> {
        val imageParts = mutableListOf<MultipartBody.Part>()

        for (bytes in images) {
            val requestFile = bytes.first.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("images", bytes.first, requestFile)
            imageParts.add(part)
        }

        return imageParts
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