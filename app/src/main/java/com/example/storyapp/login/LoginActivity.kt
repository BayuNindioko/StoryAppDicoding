package com.example.storyapp.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels

import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.main.MainActivity

import com.example.storyapp.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private var isMyEditTextValid = false
    private var isEmailEditTextValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (isLoggedIn()) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.loginResult.observe(this) { loginResponse ->
            val token = loginResponse?.loginResult?.token
            saveSessionData(token!!)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(
                this@LoginActivity,
                "Log In Successful",
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.visibility = View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(
                this@LoginActivity,
                error,
                Toast.LENGTH_SHORT
            ).show()
            binding.progressBar.visibility = View.GONE
        }

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            login(email, password)
        }

        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isMyEditTextValid = binding.edLoginPassword.error.isNullOrEmpty()
                binding.loginButton.isEnabled = isMyEditTextValid
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEmailEditTextValid = binding.edLoginEmail.error.isNullOrEmpty()
                binding.loginButton.isEnabled = isEmailEditTextValid
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        playAnimation()

    }

    private fun login(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.login(email, password)
    }

    private fun saveSessionData(token: String) {
        val sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.putBoolean("IS_LOGGED_IN", true)
        editor.apply()
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("SESSION_DATA", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false)
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val image = ObjectAnimator.ofFloat(binding.imageView, View.ALPHA, 1f).setDuration(200)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val signupText = ObjectAnimator.ofFloat(binding.signupText, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(image,emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout,login,signupText,signup)
            startDelay = 500
        }.start()
    }
}