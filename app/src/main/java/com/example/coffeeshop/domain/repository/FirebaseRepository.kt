package com.example.coffeeshop.domain.repository

import com.example.coffeeshop.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus

interface FirebaseRepository {
    suspend fun checkStatus(): AuthState
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun signOut(): AuthState
    suspend fun getUserName(): String
}