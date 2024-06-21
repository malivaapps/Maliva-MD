package com.example.maliva.data.di

import android.content.Context
import com.example.maliva.data.api.ApiConfig
import com.example.maliva.data.preference.LoginPreferences
import com.example.maliva.data.preference.dataStore
import com.example.maliva.data.repository.DestinationRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): DestinationRepository {
        val pref = LoginPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().first() }
        val apiService = ApiConfig.getApiService(user.toString())
        return DestinationRepository.getInstance(apiService, pref)
    }

    fun provideLoginPreferences(context: Context): LoginPreferences {
        return LoginPreferences.getInstance(context.dataStore)
    }
}