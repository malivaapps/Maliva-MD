package com.example.maliva.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.ProfileResponse
import com.example.maliva.data.response.UpdateProfile
import kotlinx.coroutines.launch
import com.example.maliva.data.state.Result

class ProfileViewModel(
    private val repository: DestinationRepository
) : ViewModel() {
    private val _updateProfileResponse = MutableLiveData<Result<UpdateProfile>>()
    val updateProfileResponse: LiveData<Result<UpdateProfile>> get() = _updateProfileResponse

    fun userProfile() {
        viewModelScope.launch {
            _updateProfileResponse.value = Result.Loading
            try {
                val result = repository.updateProfile()
                _updateProfileResponse.value = result.value
            } catch (e: Exception) {
                _updateProfileResponse.value = Result.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}