package com.example.maliva.view.profilelogin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maliva.data.preference.UserModel
import com.example.maliva.data.repository.DestinationRepository
import com.example.maliva.data.preference.LoginPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class ProfileLoginViewModel(
    private val destinationRepository: DestinationRepository,
    private val loginPreferences: LoginPreferences
) : ViewModel() {

    private val _userData = MutableLiveData<UserModel>()
    val userData: LiveData<UserModel> = _userData

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            val isLoggedIn = loginPreferences.getLoginStatus().first() ?: false
            _isLoggedIn.value = isLoggedIn
        }
    }

//    fun getUserData() {
//        viewModelScope.launch {
//            try {
//                val user = destinationRepository.getUserData()
//                _userData.value = UserModel(user.token, user.username, user.email, "", true)
//                // ^ Adjusted to match the UserModel constructor parameters
//            } catch (e: IOException) {
//                _userData.value = UserModel("Network Error", "", "", "", false)
//            } catch (e: Exception) {
//                _userData.value = UserModel("Unknown Error", "", "", "", false)
//            }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            destinationRepository.logout()
        }
    }

}
