package com.example.storyapp

import com.example.storyapp.model.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = mutableListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "photoUrl +$i",
                "createdAt + $i",
                "name + $i",
                "description +$i",
                i.toDouble(),
                "id + $i",
                i.toDouble(),
            )
            items.add(quote)
        }
        return items
    }
}