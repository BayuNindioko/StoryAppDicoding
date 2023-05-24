package com.example.storyapp.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailBinding


@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")

        supportActionBar?.title= "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        detailViewModel.detailStory.observe(this) { detailStory ->
            if (detailStory != null) {
                val story = detailStory.story
                Glide.with(this)
                    .load(story.photoUrl)
                    .into(binding.ivDetailPhoto)
                binding.tvDetailName.text = resources.getString(R.string.name_story, story.name)
                binding.tvDetailDescription.text = story.description
            }
        }

        detailViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        val sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")

        if (id != null) {
            detailViewModel.getDetailStory(token!!, id)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}