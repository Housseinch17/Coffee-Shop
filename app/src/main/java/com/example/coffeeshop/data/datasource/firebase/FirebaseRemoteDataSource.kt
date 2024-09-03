package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus

interface FirebaseRemoteDataSource {
    suspend fun checkStatus(): AuthState
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun signOut(): AuthState
    suspend fun getUsername(): String
}