package com.example.maliva.data.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
