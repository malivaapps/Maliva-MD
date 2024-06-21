package com.example.maliva.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.state.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<SignInResponse>> {
        return destinationRepository.login(email, password)
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            destinationRepository.saveToken(token)
            destinationRepository.loginPref()
        }
    }
    fun saveUsername(username: String) {
        viewModelScope.launch {
            destinationRepository.saveUsername(username)
        }
    }

    fun saveEmail(email: String) {
        viewModelScope.launch {
            destinationRepository.saveEmail(email)
        }
    }
}