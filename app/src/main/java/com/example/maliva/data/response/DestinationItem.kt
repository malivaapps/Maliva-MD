package com.example.maliva.data.response

import android.os.Parcelable

interface DestinationItem : Parcelable {
    val id: String?
    val title: String?
    val location: String?
    val price: Int?
    val rating: Float?
    val imageUrl: String?
}