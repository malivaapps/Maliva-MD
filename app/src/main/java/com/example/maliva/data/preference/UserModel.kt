package com.example.maliva.data.preference

data class UserModel (
    val token: String,
    val email: String,
    val password: String,
    val isLogin: Boolean = false
)