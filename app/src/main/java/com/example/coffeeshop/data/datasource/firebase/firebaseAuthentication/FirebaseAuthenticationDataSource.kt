package com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication

import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus

interface FirebaseAuthenticationDataSource {
    suspend fun getCurrentUser(): String?
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun signOut()
}