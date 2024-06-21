package com.example.maliva.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchRecomendation::class], version = 1)
abstract class SearchRecomendationsDatabase : RoomDatabase() {
    abstract fun recommendationDao(): SearchRecomendationDao

    companion object {
        @Volatile
        private var INSTANCE: SearchRecomendationsDatabase? = null

        fun getDatabase(context: Context): SearchRecomendationsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchRecomendationsDatabase::class.java,
                    "maliva_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}