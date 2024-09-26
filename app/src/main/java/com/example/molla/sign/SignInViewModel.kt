package com.example.molla.sign

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.MollaApp
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.LoginRequest
import com.example.molla.api.dto.response.LoginSuccessResponse
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class SignInViewModel : ViewModel() {

    fun login(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

        val loginRequest = LoginRequest(email, password)
        val retrofit = ApiClient.apiService.login(loginRequest)

        viewModelScope.launch {
            retrofit.enqueue(object: Callback<StandardResponse<LoginSuccessResponse>> {
                override fun onResponse(
                    call: Call<StandardResponse<LoginSuccessResponse>>,
                    response: Response<StandardResponse<LoginSuccessResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val successResponse = response.body()
                        successResponse?.data?.let { loginData ->
                            MollaApp.instance.userId = loginData.id
                            MollaApp.instance.username = loginData.username
                            MollaApp.instance.email = loginData.email
                            onSuccess()
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
                                //Log.d()
                            } catch (e: Exception) {
                                onError("Json Parsing Error: ${e.message}")
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<StandardResponse<LoginSuccessResponse>>,
                    t: Throwable
                ) {
                    onError(t.message ?: " Network error")
                }
            })

        }
    }
}