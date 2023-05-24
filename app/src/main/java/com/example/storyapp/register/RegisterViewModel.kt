package com.example.storyapp.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    val registerResponse = MutableLiveData<RegisterResponse?>()
    val isLoading = MutableLiveData<Boolean>()

    fun register(name: String, email: String, password: String) {
        isLoading.value = true
        val apiService = ApiConfig().getApiService()
        apiService.register(name, email, password).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    registerResponse.value = response.body()
                } else {
                    registerResponse.value = null
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registerResponse.value = null
                isLoading.value = false
            }
        })
    }
}