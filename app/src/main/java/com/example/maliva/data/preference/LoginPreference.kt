package com.example.maliva.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_preferences")

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val loginToken = stringPreferencesKey("token")
    private val loginStatus = booleanPreferencesKey("status")
    private val queryKey = stringPreferencesKey("last_query")
    private val usernameKey = stringPreferencesKey("username")
    private val emailKey = stringPreferencesKey("email")

    suspend fun saveLastQuery(query: String) {
        dataStore.edit { preferences ->
            preferences[queryKey] = query
        }
    }

    fun getLastQuery(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[queryKey]
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[loginToken] = token
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[usernameKey] = username
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    suspend fun loginPref() {
        dataStore.edit { preferences ->
            preferences[loginStatus] = true
        }
    }

    fun getLoginStatus(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[loginStatus]
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[loginToken]
        }
    }

    fun getUsername(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[usernameKey]
        }
    }

    fun getEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[emailKey]
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[loginStatus] = false
            preferences[loginToken] = ""
            preferences[usernameKey] = ""
            preferences[emailKey] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
