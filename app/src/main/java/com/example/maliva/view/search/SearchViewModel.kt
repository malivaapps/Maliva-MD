package com.example.maliva.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.state.Result
import com.example.maliva.data.repository.DestinationRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    private val _filteredDestinations = MutableLiveData<Result<List<DataItem>>>()
    val filteredDestinations: LiveData<Result<List<DataItem>>> get() = _filteredDestinations

    private val _searchResults = MutableLiveData<Result<List<DataItem>>>()
    val searchResults: LiveData<Result<List<DataItem>>> get() = _searchResults

    fun searchDestinations(search: String) {
        viewModelScope.launch {
            _searchResults.value = Result.Loading
            try {
                val result = destinationRepository.searchDestinations(search)
                _searchResults.value = result
            } catch (e: Exception) {
                _searchResults.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun filterDestinationsByCategory(category: String) {
        viewModelScope.launch {
            _filteredDestinations.value = Result.Loading
            try {
                val result = destinationRepository.filterDestinationsByCategory(category)
                _filteredDestinations.value = result
            } catch (e: Exception) {
                _filteredDestinations.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun filterDestinations(query: String) {
        viewModelScope.launch {
            _filteredDestinations.value = Result.Loading
            try {
                val result = destinationRepository.filterDestinationsByQuery(query)
                _filteredDestinations.value = result
            } catch (e: Exception) {
                _filteredDestinations.value = Result.Error(e.message ?: "Unknown error")
            }
        }
    }
}