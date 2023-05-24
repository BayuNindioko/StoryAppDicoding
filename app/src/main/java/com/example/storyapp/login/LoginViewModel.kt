package com.example.storyapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.LoginRequest
import com.example.storyapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    val loginResult = MutableLiveData<LoginResponse>()
    val errorMessage = MutableLiveData<String>()

    fun login(email: String, password: String) {
        val apiService = ApiConfig().getApiService()
        val loginRequest = LoginRequest(email, password)

        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    loginResult.value = response.body()
                } else {
                    errorMessage.value = when (response.code()) {
                        401 -> "Unauthorized"
                        422 -> response.body()?.message ?: "Invalid password"
                        else -> "Login failed: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                errorMessage.value = "Login failed: ${t.message}"
            }
        })
    }
}