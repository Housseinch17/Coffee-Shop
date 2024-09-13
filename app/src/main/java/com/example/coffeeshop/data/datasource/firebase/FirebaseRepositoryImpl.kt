package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.data.datasource.firebase.firebaseAuthentication.FirebaseAuthenticationDataSourceImpl
import com.example.coffeeshop.data.datasource.firebase.firebaseReadData.FirebaseReadDataDataSourceImpl
import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.ui.screen.homepage.FirebaseCategoryResponse
import com.example.coffeeshop.ui.screen.homepage.FirebaseOffersResponse
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuthenticationDataSourceImpl: FirebaseAuthenticationDataSourceImpl,
    private val firebaseReadDataDataSourceImpl: FirebaseReadDataDataSourceImpl
): FirebaseRepository{
    override suspend fun getCurrentUser(): String? {
        return firebaseAuthenticationDataSourceImpl.getCurrentUser()
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseAuthenticationDataSourceImpl.logIn(email,password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseAuthenticationDataSourceImpl.signUp(email,password)
    }

    override suspend fun signOut() {
         firebaseAuthenticationDataSourceImpl.signOut()
    }

    override suspend fun readCategoryDataFromFirebase(): FirebaseCategoryResponse {
        return firebaseReadDataDataSourceImpl.readCategoryDataFromFirebase()
    }

    override suspend fun readOffersDataFromFirebase(): FirebaseOffersResponse {
        return firebaseReadDataDataSourceImpl.readOffersDataFromFirebase()
    }
}