package com.example.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.model.ListStoryItem
import com.example.storyapp.model.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: LiveData<List<ListStoryItem>> = _stories


    fun getAllStoriesWithLocation(token: String) {
        val apiConfig = ApiConfig()
        val apiService = apiConfig.getApiService()

        val page = 1
        val size = 100
        val location = 1

        val call = apiService.getLocation("Bearer $token", page, size, location)

        call.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val stories = response.body()?.listStory
                    _stories.value = stories!!
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {

            }
        })
    }
}