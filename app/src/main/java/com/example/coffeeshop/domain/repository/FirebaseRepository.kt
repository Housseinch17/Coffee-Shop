package com.example.coffeeshop.domain.repository

import com.example.coffeeshop.ui.screen.homepage.FirebaseCategoryResponse
import com.example.coffeeshop.ui.screen.homepage.FirebaseOffersResponse
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus

interface FirebaseRepository {
    suspend fun getCurrentUser(): String?
    suspend fun logIn(email: String,password: String): AuthState
    suspend fun signUp(email: String,password: String): AccountStatus
    suspend fun signOut()
    suspend fun readCategoryDataFromFirebase(): FirebaseCategoryResponse
    suspend fun readOffersDataFromFirebase(): FirebaseOffersResponse
}