package com.example.maliva.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDestinationDao {

    @Query("SELECT * FROM favorite_destinations")
    fun getSavedDestinations(): LiveData<List<FavoriteDestination>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDestination(destination: FavoriteDestination)


    @Query("DELETE FROM favorite_destinations WHERE id = :id")
    suspend fun deleteDestination(id: String)

    @Query("SELECT * FROM favorite_destinations WHERE id = :id LIMIT 1")
    suspend fun isFavorite(id: String): FavoriteDestination?
}
