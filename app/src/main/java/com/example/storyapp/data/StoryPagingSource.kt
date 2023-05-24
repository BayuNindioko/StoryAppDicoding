package com.example.storyapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.api.ApiService
import com.example.storyapp.model.ListStoryItem



class StoryPagingSource(private val apiService: ApiService, private val token:String) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {

        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStories(token, page = page, size = params.loadSize).listStory
            Log.d("Story", response.toString())
            LoadResult.Page(
                data = response,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("Story", e.toString())
            LoadResult.Error(e)
        }
    }
}