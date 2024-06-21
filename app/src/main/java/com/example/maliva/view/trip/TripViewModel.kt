package com.example.maliva.view.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.TripPlanDetailResponse
import com.example.maliva.data.state.Result
import kotlinx.coroutines.Dispatchers

class TripViewModel(private val repository: DestinationRepository) : ViewModel() {

    // LiveData builder inside ViewModel to fetch trip plan data
    fun getTripPlan(): LiveData<Result<TripPlanDetailResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = repository.getTripPlan() // Assuming repository method returns Result<TripPlanDetailResponse>
            emit(response)
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }
}
