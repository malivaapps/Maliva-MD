package com.example.maliva.data.repository

import android.content.Context
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
import com.example.maliva.data.response.ReviewUploadRequest
import com.example.maliva.data.response.ReviewsResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.response.UploadReviewResponse
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.maliva.data.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun uploadImage(
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

    fun uploadReview(
    destinationId: String,
    rating: Int,
    review: String
    ): LiveData<Result<UploadReviewResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val token = loginPreferences.getToken().first()

            // Log the token
            Log.d("DestinationRepository", "Token: $token")

            if (token != null) {
                // Update apiService with token
                apiService = ApiConfig.getApiService(token)

                // Log the request details
                Log.d("DestinationRepository", "Uploading review: Destination ID: $destinationId, Rating: $rating, Review: $review")

                // Call the API using ApiService
                val response = apiService.uploadReviews(
                    destinationId,
                    ReviewUploadRequest(rating, review)
                )

                // Log the response
                Log.d("DestinationRepository", "Response: $response")

                emit(Result.Success(response))
            } else {
                emit(Result.Error("Token is null"))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, UploadReviewResponse::class.java)
            val errorMessage = error.message ?: "Error: ${e.code()}"
            Log.d("DestinationRepository", "Uploading review: Destination ID: $destinationId, Rating: $rating, Review: $review")
            Log.d("DestinationRepository", "Response: $response")

            // Log detailed response
            Log.e("DestinationRepository", "HTTP Exception: Code ${e.code()}, Message: $errorMessage, Response: $response")

            // Emit detailed error message
            emit(Result.Error("HTTP Exception: Code ${e.code()}, Message: $errorMessage, Response: $response"))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unknown error occurred"
            Log.e("DestinationRepository", "Exception: $errorMessage", e)
            emit(Result.Error(errorMessage))
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
