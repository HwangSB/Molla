package com.example.molla.sign

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.SignUpRequest
import com.example.molla.api.dto.response.common.ErrorResponse
import com.example.molla.api.dto.response.common.StandardResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    fun signUp(username: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

        val signUpRequest = SignUpRequest(username, email, password)
        val retrofit = ApiClient.apiService.signup(signUpRequest)

        viewModelScope.launch {
            retrofit.enqueue(object: Callback<StandardResponse<Long>> {
                override fun onResponse(
                    call: Call<StandardResponse<Long>>,
                    response: Response<StandardResponse<Long>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val successResponse = response.body()
                        successResponse?.data?.let { it ->
                            Log.d("회원가입", "회원가입 성공 : $it")
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
                                Log.e("회원가입",
                                    "회원가입 실패: [Status] $status - [Error Response]: $errorMessage, Fields: $fieldErrors"
                                )
                            } catch (e: Exception) {
                                onError("Json Parsing Error: ${e.message}")
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<StandardResponse<Long>>, t: Throwable) {
                    Log.e("회원가입", "회원가입 실패: ${t.message ?: "Network Error"}")
                    onError(t.message ?: " Network Error")
                }

            })
        }
    }

    fun validateInput(username: String, email: String, password: String, checkedPassword: String): String? {
        if (username.isBlank()) return "이름을 입력해주세요."
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "유요한 이메일을 입력해주세요."
        }
        if (password.isBlank()) return "비밀번호를 입력해주세요"
        if (checkedPassword.isBlank()) return "비밀번호 확인 란을 입력해주세요."
        if (!password.equals(checkedPassword)) return "비밀번호가 일치하지 않습니다."

        return null
    }
}