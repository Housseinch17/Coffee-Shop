package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.AuthenticationStatus
import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSourceImpl: FirebaseRemoteDataSourceImpl
): FirebaseRepository{
    override suspend fun getCurrentUser(): String? {
        return firebaseRemoteDataSourceImpl.getCurrentUser()
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseRemoteDataSourceImpl.logIn(email,password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseRemoteDataSourceImpl.signUp(email,password)
    }

    override suspend fun signOut(): AuthenticationStatus {
        return firebaseRemoteDataSourceImpl.signOut()
    }
}