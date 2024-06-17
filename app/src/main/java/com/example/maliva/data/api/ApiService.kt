package com.example.maliva.data.api

import android.telecom.Call
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

}