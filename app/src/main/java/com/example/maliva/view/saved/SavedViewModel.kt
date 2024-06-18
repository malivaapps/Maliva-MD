package com.example.maliva.view.saved

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.database.FavoriteDestination
import com.example.maliva.data.database.FavoriteDestinationDatabase
import com.example.maliva.data.repository.FavoriteDestinationRepository
import kotlinx.coroutines.launch

class SavedViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoriteDestinationRepository
    val savedDestinations: LiveData<List<FavoriteDestination>>

    init {
        val favoriteDestinationDao = FavoriteDestinationDatabase.getDatabase(application).favoriteDestinationDao()
        repository = FavoriteDestinationRepository(favoriteDestinationDao)
        savedDestinations = repository.allFavorites
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

    suspend fun isFavorite(id: String): Boolean {
        return repository.isFavorite(id)
    }
}
