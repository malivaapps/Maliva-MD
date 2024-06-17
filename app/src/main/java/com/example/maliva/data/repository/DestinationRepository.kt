package com.example.maliva.data.repository

import android.content.Context
import android.media.Rating
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.maliva.R
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.api.ApiService
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.utils.reduceFileImage
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.ReviewsResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.response.UploadReviewResponse
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.maliva.data.state.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DestinationRepository (
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences
) {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<SignUpResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.signup(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, SignUpResponse::class.java)
            val errorMessage = error.message ?: "Unknown error"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun login(email: String, password: String): LiveData<Result<SignInResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.signin(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, SignInResponse::class.java)
            val errorMessage = error.message ?: "Unknown error"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    suspend fun saveToken(token: String) = loginPreferences.saveToken(token)

    suspend fun loginPref() = loginPreferences.loginPref()

    fun getLoginStatus() = loginPreferences.getLoginStatus()

    suspend fun logout() = loginPreferences.logout()

    fun getAllDestination(): LiveData<Result<DestinationResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                loginPreferences.getToken().first()
            }
            if (token != null) {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getAllDestination()
                emit(Result.Success(response))
            } else {
                emit(Result.Error("Token is null"))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, DestinationResponse::class.java)
            emit(Result.Error(error.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
    fun getDestinationGallery(destinationId: String): LiveData<Result<GalleryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                loginPreferences.getToken().first()
            }
            if (token != null) {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getDestinationGallery(destinationId)
                emit(Result.Success(response))
            } else {
                Log.e("GalleryViewModel", "Token is null")
                emit(Result.Error("Token is null"))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, GalleryResponse::class.java)
            emit(Result.Error(error.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDestinationReviews(reviewsId: String): LiveData<Result<ReviewsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                loginPreferences.getToken().first()
            }
            if (token != null) {
                val apiService = ApiConfig.getApiService(token)
                val response = apiService.getDestinationReviews(reviewsId)
                emit(Result.Success(response))
            } else {
                Log.e("DestinationRepository", "Token is null")
                emit(Result.Error("Token is null"))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, ReviewsResponse::class.java)
            emit(Result.Error(error.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun uploadImage(
        context: Context,
        destinationId: String,
        file: File?,
    ): LiveData<Result<UploadImageResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { loginPreferences.getToken().first() }
            val service = ApiConfig.getApiService(token.toString())
            if (file != null) {
                val reducedFile = file.reduceFileImage()
                val imageFile = reducedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData("image", reducedFile.name, imageFile)
                val response = service.uploadImage(destinationId, imageMultipart)
                emit(Result.Success(response))
            } else {
                emit(Result.Error(context.getString(R.string.empty_image)))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, UploadImageResponse::class.java)
            emit(Result.Error(error.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun uploadReview(
        context: Context,
        destinationId: String,
        rating: Int,
        review: String
    ): LiveData<Result<UploadReviewResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking { loginPreferences.getToken().first() }
            val service = ApiConfig.getApiService(token.toString())

            val ratingPart = MultipartBody.Part.createFormData("rating", rating.toString())
            val reviewPart = MultipartBody.Part.createFormData("review", review)

            val response = service.uploadReviews(destinationId, ratingPart, reviewPart)
            emit(Result.Success(response))

        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, UploadReviewResponse::class.java)
            emit(Result.Error(error.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    companion object {
        @Volatile
        private var instance: DestinationRepository? = null

        fun getInstance(
            apiService: ApiService,
            preferences: LoginPreferences,
        ): DestinationRepository =
            instance ?: synchronized(this) {
                instance ?: DestinationRepository(
                    apiService,
                    preferences
                ).also {
                    instance = it
                }
            }
    }
}
