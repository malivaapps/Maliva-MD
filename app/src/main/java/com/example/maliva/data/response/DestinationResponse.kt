package com.example.maliva.data.response

import com.google.gson.annotations.SerializedName

data class DestinationResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Location(

	@field:SerializedName(" Place")
	val place: String? = null
)

data class DataItem(

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("Category")
	val category: String? = null,

	@field:SerializedName("Accessibility")
	val accessibility: String? = null,

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("Images")
	val images: String? = null,

	@field:SerializedName("Rating")
	val rating: Any? = null,

	@field:SerializedName("Facilities")
	val facilities: String? = null,

	@field:SerializedName("Pricing")
	val pricing: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("Location ")
	val location: Location? = null,

	@field:SerializedName("Activies")
	val activies: String? = null,

	@field:SerializedName("Link")
	val link: String? = null,

	@field:SerializedName("Destination name")
	val destinationName: String? = null
)