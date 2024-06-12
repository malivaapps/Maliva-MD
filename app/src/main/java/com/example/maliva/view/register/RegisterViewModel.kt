package com.example.maliva.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.SignUpResponse
import com.example.maliva.data.state.Result

class RegisterViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {
    fun register(
        username: String,
        email: String,
        password: String
    ): LiveData<Result<SignUpResponse>> {
        return destinationRepository.register(username, email, password)
    }
}