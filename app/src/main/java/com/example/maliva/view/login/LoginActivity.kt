package com.example.maliva.view.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.maliva.R
import com.example.maliva.databinding.ActivityLoginBinding
import com.example.maliva.view.register.RegisterActivity
import com.example.maliva.data.state.Result
import com.example.maliva.data.utils.ObtainViewModelFactory
import com.example.maliva.data.utils.setMotionVisibilities
import com.example.maliva.view.main.MainActivity
import android.util.Log
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPreferences: LoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreferences = LoginPreferences.getInstance(this.dataStore)

        val viewModel = ObtainViewModelFactory.obtain<LoginViewModel>(this)

        val alertBuilder = AlertDialog.Builder(this)

        binding.tvDirectToRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.setMotionVisibilities(View.VISIBLE)
                            binding.loginButton.isClickable = false
                        }
                        is Result.Success -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.loginButton.isClickable = true

                            Log.d("LoginActivity", "API response: ${result.data}")
                            val session = result.data.data?.session
                            if (session != null) {
                                // Simpan token, username, dan email ke DataStore
                                viewModel.saveToken(session)
                                viewModel.saveUsername(result.data.data.username.toString())
                                viewModel.saveEmail(result.data.data.email.toString())

                                alertBuilder.setTitle(getString(R.string.loginSuccess))
                                alertBuilder.setMessage(getString(R.string.startExplore))
                                alertBuilder.setPositiveButton(getString(R.string.ok)) { _, _ ->
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finishAffinity()
                                }.setOnCancelListener {
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finishAffinity()
                                }.create().show()
                            } else {
                                alertBuilder.setTitle(getString(R.string.loginError))
                                alertBuilder.setMessage("Session token is missing")
                                alertBuilder.setPositiveButton(R.string.ok) { _, _ -> }.create().show()
                            }
                        }
                        is Result.Error -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.loginButton.isClickable = true
                            alertBuilder.setTitle(getString(R.string.loginError))
                            alertBuilder.setMessage(result.error)
                            alertBuilder.setPositiveButton(R.string.ok) { _, _ -> }.create().show()
                        }
                    }
                }
            }
        }
    }
}