package com.example.maliva.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewsResponse(
    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("data")
    val data: List<ReviewsItem>? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class ReviewsItem(
    @SerializedName("rating")
    val rating: Float? = null,

    @SerializedName("username")
    val username: Float? = null,
    @SerializedName("userID")
    val userID: String? = null,

    @SerializedName("review")
    val review: String? = null,

    @SerializedName("createAt")
    val createAt: Long? = null
) : Parcelable