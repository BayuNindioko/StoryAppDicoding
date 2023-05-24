package com.example.storyapp.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.DetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val apiService = ApiConfig().getApiService()

    val detailStory = MutableLiveData<DetailResponse>()
    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun getDetailStory(token: String, id: String) {
        isLoading.value = true
        apiService.getDetail("Bearer $token", id).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(call: Call<DetailResponse>, response: Response<DetailResponse>) {
                if (response.isSuccessful) {
                    val story = response.body()?.story
                    if (story != null) {
                        detailStory.value = response.body()
                    }
                } else {
                    errorMessage.value = "Failed to fetch story detail"
                }
                isLoading.value = false
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                errorMessage.value = "Failed to fetch story detail"
                isLoading.value = false
            }
        })
    }
}