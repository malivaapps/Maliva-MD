package com.example.maliva.view.register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.maliva.R
import com.example.maliva.data.utils.ObtainViewModelFactory
import com.example.maliva.databinding.ActivityRegisterBinding
import com.example.maliva.view.login.LoginActivity
import com.example.maliva.data.state.Result
import com.example.maliva.data.utils.setMotionVisibilities

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtain<RegisterViewModel>(this)
        val alertBuilder = AlertDialog.Builder(this)

        binding.tvDirectToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.register(username, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.setMotionVisibilities(View.VISIBLE)
                            binding.signupButton.isClickable = false
                        }
                        is Result.Success -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.signupButton.isClickable = true
                            alertBuilder.setTitle(getString(R.string.registerSuccess))
                            alertBuilder.setMessage(getString(R.string.startExplore))
                            alertBuilder.setPositiveButton(getString(R.string.loginNow)) { _, _ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
                            }.create().show()
                        }
                        is Result.Error -> {
                            binding.progressBar.setMotionVisibilities(View.GONE)
                            binding.signupButton.isClickable = true
                            alertBuilder.setTitle(getString(R.string.registerError))
                            alertBuilder.setMessage(result.error)
                            alertBuilder.setPositiveButton("OK") { _, _ -> }.create().show()
                        }
                    }
                }
            }
        }
    }
}
