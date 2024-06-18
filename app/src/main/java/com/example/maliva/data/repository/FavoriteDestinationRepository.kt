package com.example.maliva.data.repository

import androidx.lifecycle.LiveData
import com.example.maliva.data.database.FavoriteDestination
import com.example.maliva.data.database.FavoriteDestinationDao

class FavoriteDestinationRepository(private val favoriteDestinationDao: FavoriteDestinationDao) {

    val allFavorites: LiveData<List<FavoriteDestination>> = favoriteDestinationDao.getSavedDestinations()

    suspend fun saveDestination(destination: FavoriteDestination) {
        favoriteDestinationDao.saveDestination(destination)
    }

    suspend fun deleteDestination(id: String) {
        favoriteDestinationDao.deleteDestination(id)
    }

    suspend fun isFavorite(id: String): Boolean {
        return favoriteDestinationDao.isFavorite(id) != null
    }
}
