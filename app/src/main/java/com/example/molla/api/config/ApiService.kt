package com.example.molla.api.config

import com.example.molla.api.dto.request.ForumCreateRequest
import com.example.molla.api.dto.request.LoginRequest
import com.example.molla.api.dto.request.SignUpRequest
import com.example.molla.websocket.dto.response.ChatHistoryResponse
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.api.dto.response.LoginSuccessResponse
import com.example.molla.api.dto.response.ForumListResponse
import com.example.molla.api.dto.response.common.PageResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.example.molla.api.dto.response.common.UpdateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * Journal
     */
    @Multipart
    @POST("api/diary/save")
    fun saveDiary(
        @Part("diary") diaryCreateRequest: RequestBody,
    ): Call<StandardResponse<Long>>

    @Multipart
    @POST("api/diary/save")
    fun saveDiary(
        @Part("diary") diaryCreateRequest: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Call<StandardResponse<Long>>

    @GET("api/diary/list/{id}")
    fun listDiaries(
        @Path("id") id: Long,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Call<StandardResponse<PageResponse<DiaryResponse>>>

    @Multipart
    @PUT("api/diary/{id}")
    fun updateDiary(
        @Path("id") id: Long,
        @Part("diary") diaryUpdateRequest: RequestBody,
        @Part updateImages: List<MultipartBody.Part>,
        @Part("deleteImages") deleteImageIds: RequestBody
    ): Call<StandardResponse<UpdateResponse>>

    @DELETE("api/diary/{id}")
    fun deleteDiary(
        @Path("id") id: Long
    ): Call<StandardResponse<Long>>

    /**
     * User
     */
    @POST("api/user/login")
    fun login(@Body request: LoginRequest): Call<StandardResponse<LoginSuccessResponse>>

    @POST("api/user/signup")
    fun signup(@Body request: SignUpRequest): Call<StandardResponse<Long>>

    /**
     * Forum
     */
    @POST("api/post/save")
    fun saveForum(@Body request: ForumCreateRequest): Call<StandardResponse<Long>>

    @GET("api/post/all")
    suspend fun getForumList(
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<StandardResponse<PageResponse<ForumListResponse>>>

    /**
     * Counsel
     */
    @GET("api/chat/history/{id}")
    fun getChatHistory(
        @Path("id") id: Long,
    ): Call<StandardResponse<List<ChatHistoryResponse>>>
}