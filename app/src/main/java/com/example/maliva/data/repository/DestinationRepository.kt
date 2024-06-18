package com.example.maliva.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.api.ApiService
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.UserModel
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import com.example.maliva.data.state.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal
import java.util.Locale.Category

class DestinationRepository(
    private var apiService: ApiService,
    private val loginPreferences: LoginPreferences
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

    suspend fun saveToken(token: String) = loginPreferences.saveToken(token)

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
