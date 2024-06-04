package com.dicoding.storyapp.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.pref.ResultValue
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.viewmodel.LoginViewModel
import com.dicoding.storyapp.viewmodel.ViewModelFactory


class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            loginButton.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                when {
                    email.isEmpty() -> {
                        edLoginEmail.error = getString(R.string.error_empty_field)
                    }

                    password.isEmpty() -> {
                        edLoginPassword.error = getString(R.string.error_empty_field)
                    }

                    password.length < 8 -> {
                        edLoginPassword.error = getString(R.string.error_password)
                    }

                    else -> login()
                }
            }
            tvSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        viewModel.login(email, password).observe(this) { result ->
            result?.let {
                when (it) {
                    is ResultValue.Loading -> showLoading(true)

                    is ResultValue.Success -> {
                        showLoading(false)
                        viewModel.saveSession(UserModel(email, it.data.loginResult.token))
                        showAlertDialog(
                            getString(R.string.success_title),
                            it.data.message,
                            getString(R.string.continue_title),
                            MainActivity::class.java,
                            it.data.loginResult.name
                        )
                    }

                    is ResultValue.Error -> {
                        showLoading(false)
                        showAlertDialog(
                            getString(R.string.failed_title),
                            it.error,
                            getString(R.string.try_again),
                            LoginActivity::class.java
                        )
                    }
                }
            }
        }
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        buttonText: String,
        targetActivity: Class<*>? = null,
        extra: String? = null
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(buttonText) { _, _ ->
                targetActivity?.let {
                    val intent = Intent(this@LoginActivity, it).apply {
                        putExtra("name", extra)
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
            setNegativeButton(android.R.string.cancel, null)
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
    }
}