package com.example.maliva.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UploadReviewResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<UserResponse?>? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable
@Parcelize
data class UserResponse(
	@SerializedName("rating")
	val rating: Int? = null,

	@SerializedName("review")
	val review: String? = null,

	@SerializedName("createAt")
	val createAt: String? = null,

	@SerializedName("id")
	val id: String? = null,

	@SerializedName("username")
	val username: String? = null,
	@SerializedName("userID")
	val userID: String? = null,

	@SerializedName("email")
	val email: String? = null
) : Parcelable
data class ReviewUploadRequest(
	@SerializedName("rating")
	val rating: Int? = null,

	@SerializedName("reviews")
	val review: String? = null
)


