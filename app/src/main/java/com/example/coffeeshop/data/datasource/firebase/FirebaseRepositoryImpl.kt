package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSourceImpl: FirebaseRemoteDataSourceImpl
): FirebaseRepository{
    override suspend fun checkStatus(): AuthState {
        return firebaseRemoteDataSourceImpl.checkStatus()
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseRemoteDataSourceImpl.logIn(email,password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseRemoteDataSourceImpl.signUp(email,password)
    }

    override suspend fun signOut(): AuthState {
        return firebaseRemoteDataSourceImpl.signOut()
    }

    override suspend fun getUserName(): String {
        return firebaseRemoteDataSourceImpl.getUsername()
    }
}