package com.example.maliva.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

data class TripPlanResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataTrip? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataTrip(

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("plan")
	val plan: List<PlanItem?>? = null

) : Parcelable

@Parcelize
data class PlanItem(

	@field:SerializedName("Alamat")
	val alamat: String? = null,

	@field:SerializedName("Kategori")
	val kategori: String? = null,

	@SerializedName("Gambar")
	val images: String? = null,

	@field:SerializedName("Jenis Wisata")
	val jenisWisata: String? = null,

	@field:SerializedName("Rating")
	val rating: Double? = null,

	@field:SerializedName("latitude")
	val latitude: BigDecimal? = null,

	@field:SerializedName("Deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("Child Friendly")
	val childFriendly: String? = null,

	@field:SerializedName("Link Alamat")
	val linkAlamat: String? = null,

	@field:SerializedName("Fasilitas Yang Tersedia")
	val fasilitasYangTersedia: String? = null,

	@field:SerializedName("Nama Wisata")
	val namaWisata: String? = null,

	@field:SerializedName("Aksesibilitas")
	val aksesibilitas: String? = null,

	@field:SerializedName("Harga")
	val harga: Int? = null,

	@field:SerializedName("longitude")
	val longitude: BigDecimal? = null
) : Parcelable
