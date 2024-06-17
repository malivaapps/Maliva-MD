package com.example.maliva.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.api.ApiService
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.response.DataItem
import com.example.maliva.data.response.DestinationResponse
import com.example.maliva.data.response.GalleryResponse
import com.example.maliva.data.response.SignInResponse
import com.example.maliva.data.response.SignUpResponse
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.maliva.data.state.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

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
