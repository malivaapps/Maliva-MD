package com.example.maliva.data.api

import android.telecom.Call
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.ReviewsResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.response.UploadReviewResponse
import com.google.android.gms.common.api.Response
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("authenticate/signup")
    suspend fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("authenticate/signin")
    suspend fun signin(
        @Field("email") email: String,
        @Field("password") password: String
    ): SignInResponse

    @GET("destination")
    suspend fun getAllDestination(): DestinationResponse

    @GET("destination/{id}/Gallery")
    suspend fun getDestinationGallery(
        @Path("id") destinationId: String
    ): GalleryResponse

    @GET("destination/{id}/Reviews")
    suspend fun getDestinationReviews(
        @Path("id") reviewsId: String
    ): ReviewsResponse

    @Multipart
    @POST("destination/{id}/Gallery")
    suspend fun uploadImage(
        @Path("id") destinationId: String,
        @Part image: MultipartBody.Part
    ): UploadImageResponse

    @Multipart
    @POST("destination/{id}/Reviews")
    suspend fun uploadReviews(
        @Path("id") destinationId: String,
        @Part rating: MultipartBody.Part,
        @Part review: MultipartBody.Part
        ): UploadReviewResponse
}