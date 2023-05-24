package com.example.storyapp.data

import com.example.storyapp.api.ApiConfig

object Injection {
    fun provideRepository(token: String): StoryRepository {
        val apiService = ApiConfig().getApiService()
        return StoryRepository(apiService, token)
    }
}