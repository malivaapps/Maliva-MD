package com.example.maliva.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize


data class Lokasi(

    @field:SerializedName(" Tempat")
    val tempat: String? = null
) : Parcelable

@Parcelize
data class RecomendationResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("recommendations")
    val recommendations: List<RecommendationsItem?>? = null
) : Parcelable

@Parcelize
data class RecommendationsItem(

    @field:SerializedName("Alamat")
    val address: String? = null,

    @field:SerializedName("Kategori")
    val category: String? = null,

    @SerializedName("id")
    val id: String? = null,

    @field:SerializedName("Lokasi ")
    val location: Lokasi? = null,

    @field:SerializedName("Jenis Wisata")
    val activities: String? = null,

    @field:SerializedName("Rating")
    val rating: Double? = null,

    @field:SerializedName("latitude")
    val lat: Double? = null,

    @field:SerializedName("Deskripsi")
    val description: String? = null,

    @field:SerializedName("Child Friendly")
    val childFriendly: String? = null,

    @field:SerializedName("Link Alamat")
    val link: String? = null,

    @field:SerializedName("Gambar")
    val images: String? = null,

    @field:SerializedName("Nama Wisata")
    val destinationName: String? = null,

    @field:SerializedName("Aksesibilitas")
    val accessibility: String? = null,

    @field:SerializedName("Harga")
    val price: Int? = null,

    @field:SerializedName("Fasilitas")
    val facilities: String? = null,

    @field:SerializedName("Rentang Harga")
    val rentangHarga: Int? = null,

    @field:SerializedName("longitude")
    val lon: Double? = null
) : Parcelable


