package com.example.maliva.view.planner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.TripPlanResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal
import com.example.maliva.data.state.Result

class PlannerViewModel(private val repository: DestinationRepository) : ViewModel() {

    private val _tripPlan = MutableLiveData<Result<TripPlanResponse>>()
    val tripPlan: LiveData<Result<TripPlanResponse>> = _tripPlan

    fun generateTripPlan(
        category: String?,
        type: String?,
        child: String?,
        budget: Int?,
        lat: BigDecimal?,
        long: BigDecimal?,
        nrec: Int?,
        title: String?,
        callback: (Result<TripPlanResponse>) -> Unit
    ) {
        // Example implementation:
        viewModelScope.launch {
            try {
                val result = repository.generateTripPlan(category, type, child, budget, lat, long, nrec, title)
                callback(result) // Pass the result directly
            } catch (e: Exception) {
                callback(Result.Error(e.message ?: "Unknown error occurred"))
            }
        }
    }
}

