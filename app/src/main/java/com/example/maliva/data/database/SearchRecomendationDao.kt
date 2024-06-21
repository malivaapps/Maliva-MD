package com.example.maliva.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchRecomendationDao {
    @Query("SELECT * FROM recommendations")
    fun getAllRecommendations(): LiveData<List<SearchRecomendation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendations(recommendations: SearchRecomendation)
}