package com.example.maliva.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.state.Result
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.DestinationResponse

class HomeViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {
    fun getAllDestination(): LiveData<Result<DestinationResponse>> {
        return destinationRepository.getAllDestination()
    }
}