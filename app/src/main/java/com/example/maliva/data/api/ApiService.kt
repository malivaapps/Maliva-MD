package com.example.maliva.data.api

import com.example.maliva.data.preference.UserModel
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.ProfileResponse
import com.example.maliva.data.response.RecomendationResponse
import com.example.maliva.data.response.ReviewUploadRequest
import com.example.maliva.data.response.ReviewsResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse

import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.response.UploadReviewResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query
import java.math.BigDecimal


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

    @DELETE("authenticate/logout")
    suspend fun logout(@Header("authorization") authHeader: String): Response<Void>

    @GET("authenticte/user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") authHeader: String
    ): UserModel

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

    @POST("destination/{id}/Reviews")
    suspend fun uploadReviews(
        @Path("id") destinationId: String,
        @Body request: ReviewUploadRequest
    ): UploadReviewResponse

    @GET("destination")
    suspend fun getCategory(
        @Query("category") category: String? = null
    ): DestinationResponse

    @GET("destination")
    suspend fun getType(
        @Query("type") type: String? = null
    ): DestinationResponse

    @GET("destination")
    suspend fun getPrice(
        @Query("minPrice") minPrice: Int? = null,
        @Query("maxPrice") maxPrice: Int? = null
    ): DestinationResponse

    @GET("destination")
    suspend fun getRating(
        @Query("rating") rating: Int? = null
    ): DestinationResponse

    @GET("destination")
    suspend fun getRange(
        @Query("lat") lat: BigDecimal? = null,
        @Query("long") long: BigDecimal? = null,
        @Query("minRange") minRange: Int? = null,
        @Query("maxRange") maxRange: Int? = null
    ): DestinationResponse

    @GET("destination?")
    suspend fun getQuery(
        @Query("search") search: String
    ): DestinationResponse

    @GET("recommendation?")
    suspend fun getqueryRecomendations(
        @Query("search") search: String
    ): RecomendationResponse

    @GET("recommendation")
    suspend fun getRecommendations(
    ): RecomendationResponse


    @GET("authenticate/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

}