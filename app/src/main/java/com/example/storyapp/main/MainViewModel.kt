package com.example.storyapp.main

import androidx.lifecycle.*
import androidx.paging.*
import com.example.storyapp.data.Injection
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.model.ListStoryItem

class MainViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}

class ViewModelFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

