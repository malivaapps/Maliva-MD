package com.example.maliva.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteDestination::class],
    version = 1
)
abstract class FavoriteDestinationDatabase : RoomDatabase() {

    abstract fun favoriteDestinationDao(): FavoriteDestinationDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteDestinationDatabase? = null

        fun getDatabase(context: Context): FavoriteDestinationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDestinationDatabase::class.java,
                    "favorite_destination_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}