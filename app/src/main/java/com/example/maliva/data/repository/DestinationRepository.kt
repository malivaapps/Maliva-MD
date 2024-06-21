package com.example.maliva.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.maliva.R
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.api.ApiService
import com.example.maliva.data.database.SearchRecomendation
import com.example.maliva.data.database.SearchRecomendationDao
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.utils.reduceFileImage

import com.example.maliva.data.preference.UserModel
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse

import com.example.maliva.data.response.ProfileResponse
import com.example.maliva.data.response.RecomendationResponse

import com.example.maliva.data.response.PlanItem
import com.example.maliva.data.response.ReviewUploadRequest
import com.example.maliva.data.response.ReviewsResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import com.example.maliva.data.response.UpdateProfile
import com.example.maliva.data.response.TripPlanDetailResponse
import com.example.maliva.data.response.TripPlanResponse

import com.example.maliva.data.response.UploadImageResponse
import com.example.maliva.data.response.UploadReviewResponse
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.maliva.data.state.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

import java.io.IOException
import java.math.BigDecimal

class DestinationRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences,

) {
    fun register(name: String, email: String, password: String): LiveData<Result<SignUpResponse>> = liveData {
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

    fun userProfile(email: String, username: String): LiveData<Result<SignInResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.signin(email, username)
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
    suspend fun saveUsername(username: String) = loginPreferences.saveUsername(username)
    suspend fun saveEmail(email: String) = loginPreferences.saveEmail(email)

    suspend fun loginPref() = loginPreferences.loginPref()

    fun getLoginStatus() = loginPreferences.getLoginStatus()

    suspend fun logout() = loginPreferences.logout()

    suspend fun getUserData(): UserModel {
        return try {
            val token = loginPreferences.getToken().first()
            if (token != null) {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getUserProfile("Bearer $token")
                UserModel(response.token, response.username, response.email, "", true)
            } else {
                UserModel("", "", "", "", false)
            }
        } catch (e: HttpException) {
            throw Exception("HTTP Error: ${e.code()}")
        } catch (e: IOException) {
            throw IOException("Network Error: ${e.message}")
        } catch (e: Exception) {
            throw Exception("Error: ${e.message}")
        }
    }

    fun getAllDestination(): LiveData<Result<DestinationResponse>> = liveData {
        emit(Result.Loading)
        try {
            val isLoggedIn = loginPreferences.getLoginStatus().first() ?: false
            if (isLoggedIn) {
                val token = loginPreferences.getToken().first()
                if (token != null) {
                    apiService = ApiConfig.getApiService(token)
                    val response = apiService.getAllDestination()
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("Token is null"))
                }
            } else {
                emit(Result.Error("User not logged in"))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, DestinationResponse::class.java)
            emit(Result.Error(error.message ?: "Unknown error"))
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

    suspend fun filterDestinationsByCategory(category: String): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getAllDestination()

                if (response.data != null) {
                    val destinations = response.data.filter { destination ->
                        destination?.activities?.contains(category) ?: false
                    }.mapNotNull { it }

                    Result.Success(destinations)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun filterDestinationByCategories(category: String): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getCategory(category) // Use the getCategory endpoint

                if (response.data != null) {
                    // Filter out null DataItem instances
                    val destinations = response.data.filterNotNull()

                    Result.Success(destinations)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun getAllCategories(): Result<List<String>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getAllDestination()

                if (response.data != null) {
                    // Extract all unique categories from DataItem list
                    val categories = response.data
                        .mapNotNull { it?.category }
                        .distinct()

                    Result.Success(categories)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun filterDestinationByType(type: String): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getType(type)

                if (response.data != null) {
                    // Filter out null DataItem instances if necessary
                    val items = response.data.filterNotNull()
                    Result.Success(items)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun getAllTypes(): Result<List<String>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getAllDestination()

                if (response.data != null) {
                    // Extract all unique activities from DataItem list
                    val activities = response.data
                        .flatMap { it?.activities?.split(",") ?: emptyList() } // Split activities if it's a comma-separated string
                        .mapNotNull { it.trim() }
                        .distinct()

                    Result.Success(activities)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }


    suspend fun filterDestinationsByQuery(query: String): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getAllDestination()
                if (response is DestinationResponse) {
                    val filteredDestinations = response.data?.filter { destination ->
                        destination?.destinationName?.contains(query, ignoreCase = true) ?: false
                    }?.mapNotNull { it }
                    if (filteredDestinations != null) {
                        Result.Success(filteredDestinations)
                    } else {
                        Result.Error("Filtered destinations are null")
                    }
                } else {
                    Result.Error("Unexpected response format")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun filterDestinationsByPrice(minPrice: Int, maxPrice: Int): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getPrice() // Replace with actual API call to fetch destinations

                if (response.data != null) {
                    val filteredDestinations = response.data.filter { destination ->
                        destination?.pricing?.let {
                            it >= minPrice && it <= maxPrice
                        } ?: false
                    }.mapNotNull { it }

                    Result.Success(filteredDestinations)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun filterDestinationsByRating(rating: Int): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getRating(rating)
                if (response.data != null) {
                    val destinations = response.data.filterNotNull()
                    Result.Success(destinations)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    suspend fun filterDestinationsByLocationAndRange(
        lat: BigDecimal,
        long: BigDecimal,
        minRange: Int,
        maxRange: Int
    ): Result<List<DataItem>> {
        return try {
            val token = loginPreferences.getToken().first()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                apiService = ApiConfig.getApiService(token)
                val response = apiService.getRange(lat, long, minRange, maxRange)

                if (response.data != null) {
                    val filteredDestinations = response.data.filterNotNull()
                    Result.Success(filteredDestinations)
                } else {
                    Result.Error("Response data is null")
                }
            }
        } catch (e: HttpException) {
            handleHttpException(e)
        } catch (e: IOException) {
            Result.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }

    private fun handleHttpException(e: HttpException): Result.Error {
        val response = e.response()
        val errorBody = response?.errorBody()?.string()
        val error = Gson().fromJson(errorBody, DestinationResponse::class.java)
        return Result.Error("HTTP Error: ${response?.code()}. ${error.message ?: "Unknown error"}")
    }

    fun updateProfile(): LiveData<Result<UpdateProfile>> = liveData {
        emit(Result.Loading)
        try {
            val token = loginPreferences.getToken().first()
            if (token != null) {
                val response = apiService.getProfile("Bearer $token")
                val updateProfile = response.data?.firstOrNull()
                if (updateProfile != null) {
                    emit(Result.Success(updateProfile))
                } else {
                    emit(Result.Error("No profile data available"))
                }
            } else {
                emit(Result.Error("Token is null"))
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, ProfileResponse::class.java)
            val errorMessage = error.message ?: "Unknown error"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    suspend fun searchDestinations(search: String): Result<List<DataItem>> {
        return try {
            val response = apiService.getQuery(search)
            Result.Success(response.data as List<DataItem>? ?: emptyList())
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorBody, DestinationResponse::class.java)
            Result.Error(error.message ?: "Unknown error")
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }

    fun getRecommendations(query: String): LiveData<Result<RecomendationResponse>> = liveData {
        Log.d(TAG, "getRecommendations: Starting request with search:")
        try {

    suspend fun generateTripPlan(
        category: String?,
        type: String?,
        child: String?,
        budget: Int?,
        lat: BigDecimal?,
        long: BigDecimal?,
        nrec: Int?,
        title: String?
    ): Result<TripPlanResponse> {
        return try {
            val token = loginPreferences.getToken().firstOrNull()
            if (token == null) {
                Result.Error("Token is null")
            } else {
                // Call the API to save the trip plan
                val response = apiService.generateTripPlan(category, type, child, budget, lat, long, nrec, title)
                Result.Success(response)
            }
        } catch (e: HttpException) {
            // Handle HTTP exceptions (like 4xx, 5xx status codes)
            val errorResponse = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorResponse, TripPlanResponse::class.java)
            Result.Error(error.message ?: "Unknown error")
        } catch (e: Exception) {
            // Handle other exceptions
            Result.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun saveTripPlan(
        category: String?,
        type: String?,
        child: String?,
        budget: Int?,
        lat: BigDecimal?,
        long: BigDecimal?,
        nrec: Int?,
        title: String?
    ): TripPlanResponse {
        return try {
            val token = loginPreferences.getToken().firstOrNull()
            if (token == null) {
                throw RuntimeException("Token is null")
            } else {
                // Call the API to save the trip plan
                apiService.saveTripPlan(category, type, child, budget, lat, long, nrec, title)
            }
        } catch (e: HttpException) {
            // Handle HTTP exceptions (like 4xx, 5xx status codes)
            val errorResponse = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorResponse, TripPlanResponse::class.java)
            throw RuntimeException(error.message ?: "Unknown error")
        } catch (e: Exception) {
            // Handle other exceptions
            throw RuntimeException(e.message ?: "Unknown error")
        }
    }

    suspend fun getTripPlan(): Result<TripPlanDetailResponse> {
        return try {
            val isLoggedIn = loginPreferences.getLoginStatus().first() ?: false
            if (isLoggedIn) {
                val token = loginPreferences.getToken().first()
                if (token != null) {
                    apiService = ApiConfig.getApiService(token)
                    val response = apiService.getqueryRecomendations(query)
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("Token is null"))
                }
            } else {
                emit(Result.Error("User not logged in"))
            }
        } catch (e: HttpException) {
            Log.e(TAG, "getRecommendations: HttpException - ${e.message}")
            emit(handleHttpException(e))
        } catch (e: IOException) {
            Log.e(TAG, "getRecommendations: IOException - ${e.message}")
            emit(Result.Error("Network Error: ${e.message}"))
        } catch (e: Exception) {
            Log.e(TAG, "getRecommendations: Exception - ${e.message}")
            emit(Result.Error("Error: ${e.message}"))
        }
    }


                    val response = apiService.getTripPlan()
                    Result.Success(response)
                } else {
                    Result.Error("Token is null")
                }
            } else {
                Result.Error("User not logged in")
            }
        } catch (e: HttpException) {
            val response = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(response, DestinationResponse::class.java)
            Result.Error(error.message ?: "Unknown error")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }


    private fun DataItem.toPlanItem(): PlanItem {
        // Example conversion, adjust fields as per your DataItem structure
        return PlanItem(namaWisata = destinationName, harga = pricing ?: 0, alamat = address ?: "")
    }


    private fun handleHttpException(e: HttpException): Result.Error {
        val response = e.response()
        val errorBody = response?.errorBody()?.string()
        val error = Gson().fromJson(errorBody, DestinationResponse::class.java)
        return Result.Error("HTTP Error: ${response?.code()}. ${error.message ?: "Unknown error"}")
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
                    preferences,
                ).also {
                    instance = it
                }
            }
    }
}




}
