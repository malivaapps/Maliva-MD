package com.example.maliva.data.api

import android.icu.text.DecimalFormat
import com.example.maliva.data.preference.UserModel
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
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

}