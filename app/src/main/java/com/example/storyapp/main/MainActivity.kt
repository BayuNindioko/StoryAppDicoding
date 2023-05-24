package com.example.storyapp.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.addStory.AddStoryActivity
import com.example.storyapp.R
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.login.LoginActivity
import com.example.storyapp.maps.MapsActivity



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!isLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        adapter = StoryAdapter()
        binding.rvStory.adapter = adapter

        binding.rvStory.layoutManager = LinearLayoutManager(this)


        val sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", "")
        val mainViewModel: MainViewModel by viewModels {
            ViewModelFactory("Bearer $token")
        }

        val adapter = StoryAdapter()

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.addLoadStateListener {
            binding.progressBar.isVisible = it.refresh is LoadState.Loading
            binding.rvStory.isVisible = it.refresh !is LoadState.Loading
        }

        mainViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.action_logOut -> {
                logout()
            }
            R.id.upload -> {
                val intent = Intent(this, AddStoryActivity::class.java)
                startActivity(intent)
            }

            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN")
        editor.remove("IS_LOGGED_IN")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(
            this@MainActivity,
            "Log Out Successful",
            Toast.LENGTH_SHORT
        ).show()
    }

}