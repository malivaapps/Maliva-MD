package com.example.maliva.view.tripresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.TripPlanResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal
import com.example.maliva.data.state.Result

class TripResultViewModel(private val repository: DestinationRepository) : ViewModel() {

    private val _saveTripPlanResult = MutableLiveData<Result<TripPlanResponse>>()
    val saveTripPlanResult: LiveData<Result<TripPlanResponse>> get() = _saveTripPlanResult

    fun saveTripPlan(
        category: String?,
        type: String?,
        child: String?,
        budget: Int?,
        lat: BigDecimal?,
        long: BigDecimal?,
        nrec: Int?,
        title: String
    ) {
        viewModelScope.launch {
            _saveTripPlanResult.value = Result.Loading
            try {
                val response = repository.saveTripPlan(category, type, child, budget, lat, long, nrec, title)
                _saveTripPlanResult.value = Result.Success(response) // Here is the issue
            } catch (e: Exception) {
                _saveTripPlanResult.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }
}
