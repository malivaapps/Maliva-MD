package com.example.maliva.data.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("session")
	val session: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
