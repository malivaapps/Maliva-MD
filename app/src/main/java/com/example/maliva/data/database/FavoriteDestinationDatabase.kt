package com.example.maliva.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [FavoriteDestination::class],
    version = 2
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // Migration from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform schema migration operations here
                database.execSQL("ALTER TABLE favorite_destinations ADD COLUMN description TEXT")
                database.execSQL("ALTER TABLE favorite_destinations ADD COLUMN facilities TEXT")
                database.execSQL("ALTER TABLE favorite_destinations ADD COLUMN accessibility TEXT")
                database.execSQL("ALTER TABLE favorite_destinations ADD COLUMN link TEXT")
            }
        }
    }
}