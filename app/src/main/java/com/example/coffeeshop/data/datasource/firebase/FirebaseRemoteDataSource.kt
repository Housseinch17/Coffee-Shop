package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.AuthenticationStatus
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus

interface FirebaseRemoteDataSource {
    suspend fun getCurrentUser(): String?
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun signOut(): AuthenticationStatus
}