package com.example.maliva.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Entity(tableName = "favorite_destinations")
@Parcelize
data class FavoriteDestination(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val destinationName: String?,
    val location: String?,
    val pricing: Int?,
    val rating: Double?,
    val imageUrl: String?
) : Parcelable

