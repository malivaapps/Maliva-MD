package com.example.maliva.view.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.database.SearchRecomendation
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.state.Result
import com.example.maliva.data.repository.DestinationRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val destinationRepository: DestinationRepository) : ViewModel() {

    private val _filteredDestinations = MutableLiveData<Result<List<DataItem>>>()
    val filteredDestinations: LiveData<Result<List<DataItem>>> get() = _filteredDestinations

    private val _searchResults = MutableLiveData<Result<List<DataItem>>>()
    val searchResults: LiveData<Result<List<DataItem>>> get() = _searchResults

    private var lastSearchQuery: String? = null

    fun searchDestinations(search: String) {
        Log.d("SearchViewModel", "Searching destinations with query: $search")
        viewModelScope.launch {
            try {
                _searchResults.postValue(Result.Loading)
                val result = destinationRepository.searchDestinations(search)
                _searchResults.postValue(result)
                lastSearchQuery = search
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Search error: ${e.message}", e)
                _searchResults.postValue(Result.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun filterDestinationsByCategory(category: String) {
        viewModelScope.launch {
            _filteredDestinations.postValue(Result.Loading)
            val result = destinationRepository.filterDestinationsByCategory(category)
            _filteredDestinations.postValue(result)
        }
    }

}


