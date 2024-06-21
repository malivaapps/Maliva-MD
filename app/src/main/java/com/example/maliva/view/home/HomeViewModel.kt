package com.example.maliva.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.example.maliva.data.database.SearchRecomendation
import com.example.maliva.data.state.Result
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.RecomendationResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    private val _recommendationsLiveData = MutableLiveData<Result<RecomendationResponse>>()
    val recommendationsLiveData: LiveData<Result<RecomendationResponse>> get() = _recommendationsLiveData

    fun getAllDestination(): LiveData<Result<DestinationResponse>> {
        return destinationRepository.getAllDestination()
    }

    fun getRecommendations(query: String? = null): LiveData<Result<RecomendationResponse>> {
        return destinationRepository.getRecommendations(query.toString())
    }


}