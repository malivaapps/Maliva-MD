package com.example.maliva.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DestinationResponse(
	@SerializedName("code")
	val code: Int? = null,

	@SerializedName("data")
	val data: List<DataItem?>? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataItem(
	@SerializedName("Deskripsi")
	val description: String? = null,

	@SerializedName("Kategori")
	val category: String? = null,

	@SerializedName("Aksesibilitas")
	val accessibility: String? = null,

	@SerializedName("Alamat")
	val address: String? = null,

	@SerializedName("Gambar")
	val images: String? = null,

	@SerializedName("Rating")
	val rating: Double? = null,

	@SerializedName("Fasilitas")
	val facilities: String? = null,

	@SerializedName("Harga")
	val pricing: Int? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("Lokasi ")
	val location: Location? = null,

	@SerializedName("Jenis Wisata")
	val activities: String? = null,

	@SerializedName("Link Alamat")
	val link: String? = null,

	@SerializedName("url")
	val url: String? = null,

	@SerializedName("Nama Wisata")
	val destinationName: String? = null,

	@SerializedName("title")
	val title: String? = null


): Parcelable

@Parcelize
data class Location(
	@SerializedName(" Tempat")
	val place: String? = null
) : Parcelable
