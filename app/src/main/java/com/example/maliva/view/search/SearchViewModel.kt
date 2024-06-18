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

    fun filterDestinationsByCategory(category: String) {
        viewModelScope.launch {
            _filteredDestinations.value = destinationRepository.filterDestinationsByCategory(category)
        }
    }

    fun filterDestinations(query: String) {
        viewModelScope.launch {
            _filteredDestinations.value = destinationRepository.filterDestinationsByQuery(query)
        }
    }

    //use this function to filtering
    fun filterDestinationByCategories(category: String) {
        viewModelScope.launch {
            _filteredDestinations.value = destinationRepository.filterDestinationByCategories(category)
        }
    }
}


