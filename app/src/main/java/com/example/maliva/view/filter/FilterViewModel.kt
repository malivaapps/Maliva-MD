package com.example.maliva.view.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.state.Result
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FilterViewModel(
    private val repository: DestinationRepository
) : ViewModel() {

    // LiveData for filtered destinations
    private val _filteredDestinations = MutableLiveData<Result<List<DataItem>>>()
    val filteredDestinations: LiveData<Result<List<DataItem>>> = _filteredDestinations

    // LiveData for all categories
    private val _allCategories = MutableLiveData<Result<List<String>>>()
    val allCategories: LiveData<Result<List<String>>> = _allCategories

    private val _allTypes = MutableLiveData<Result<List<String>>>()
    val allTypes: LiveData<Result<List<String>>> = _allTypes



    // Function to filter destinations by category
    suspend fun filterDestinationsByCategory(category: String) {
        _filteredDestinations.value = Result.Loading
        try {
            val result = repository.filterDestinationByCategories(category)
            _filteredDestinations.value = result
        } catch (e: Exception) {
            _filteredDestinations.value = Result.Error("An unknown error occurred: ${e.message}")
        }
    }



    // Function to fetch all categories
    fun fetchAllCategories() {
        viewModelScope.launch {
            _allCategories.value = Result.Loading
            try {
                val result = repository.getAllCategories()
                _allCategories.value = result
            } catch (e: Exception) {
                _allCategories.value = Result.Error("An unknown error occurred: ${e.message}")
            }
        }
    }

    suspend fun filterDestinationByType(type: String) {
        _filteredDestinations.value = Result.Loading
        try {
            val result = repository.filterDestinationByType(type)
            _filteredDestinations.value = result
        } catch (e: Exception) {
            _filteredDestinations.value = Result.Error("An unknown error occurred: ${e.message}")
        }
    }

    fun fetchAllTypes() {
        viewModelScope.launch {
            _allTypes.value = Result.Loading
            try {
                val result = repository.getAllTypes()
                _allTypes.value = result
            } catch (e: Exception) {
                _allTypes.value = Result.Error("An unknown error occurred: ${e.message}")
            }
        }
    }

    suspend fun filterDestinationsByPrice(minPrice: Int, maxPrice: Int) {
        _filteredDestinations.value = Result.Loading
        try {
            val result = repository.filterDestinationsByPrice(minPrice, maxPrice)
            _filteredDestinations.value = result
        } catch (e: Exception) {
            _filteredDestinations.value = Result.Error("An unknown error occurred: ${e.message}")
        }
    }

    suspend fun filterDestinationsByRating(rating: Int) {
        _filteredDestinations.value = Result.Loading
        try {
            val result = repository.filterDestinationsByRating(rating)
            _filteredDestinations.value = result
        } catch (e: Exception) {
            _filteredDestinations.value = Result.Error("An unknown error occurred: ${e.message}")
        }
    }

    suspend fun filterDestinationsByLocationAndRange(
        lat: BigDecimal,
        long: BigDecimal,
        minRange: Int,
        maxRange: Int
    ) {
        _filteredDestinations.value = Result.Loading
        try {
            val result = repository.filterDestinationsByLocationAndRange(lat, long, minRange, maxRange)
            _filteredDestinations.value = result
        } catch (e: Exception) {
            _filteredDestinations.value = Result.Error("An unknown error occurred: ${e.message}")
        }
    }

}
