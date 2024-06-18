package com.example.maliva.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.database.FavoriteDestination
import com.example.maliva.data.repository.FavoriteDestinationRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: FavoriteDestinationRepository) : ViewModel() {

    suspend fun isFavorite(id: String): Boolean {
        return repository.isFavorite(id)
    }

    fun saveDestination(destination: FavoriteDestination) {
        viewModelScope.launch {
            repository.saveDestination(destination)
        }
    }

    fun deleteDestination(id: String) {
        viewModelScope.launch {
            repository.deleteDestination(id)
        }
    }
}
