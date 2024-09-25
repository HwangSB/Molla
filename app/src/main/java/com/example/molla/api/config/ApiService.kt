package com.example.molla.api.config

import com.example.molla.api.dto.request.DiaryCreateRequest
import com.example.molla.api.dto.request.DiaryUpdateRequest
import com.example.molla.api.dto.response.DiaryCreateResponse
import com.example.molla.api.dto.response.DiaryDeleteResponse
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.api.dto.response.DiaryUpdateResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

public interface ApiService {
    @POST("api/diary/save")
    fun saveDiary(@Body diaryCreateRequest: DiaryCreateRequest): Call<DiaryCreateResponse>

    @GET("api/diary/list/{id}")
    fun listDiary(@Path("id") id: Long, @Query("pageNumber") pageNumber: Int, @Query("pageSize") pageSize: Int): Call<List<DiaryResponse>>

    @PUT("api/diary/{id}")
    fun updateDiary(@Path("id") id: Long, @Body diaryUpdateRequest: DiaryUpdateRequest): Call<DiaryUpdateResponse>

    @DELETE("api/diary/{id}")
    fun deleteDiary(@Path("id") id: Long): Call<DiaryDeleteResponse>
}