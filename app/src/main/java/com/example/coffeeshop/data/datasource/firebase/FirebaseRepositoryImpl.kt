package com.example.coffeeshop.data.datasource.firebase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.ui.screen.login.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuthenticationRemoteDataSourceImpl: FirebaseAuthenticationDataSourceImpl
): FirebaseRepository{
    override suspend fun getCurrentUser(): String? {
        return firebaseAuthenticationRemoteDataSourceImpl.getCurrentUser()
    }

    override suspend fun logIn(email: String, password: String): AuthState {
        return firebaseAuthenticationRemoteDataSourceImpl.logIn(email,password)
    }

    override suspend fun signUp(email: String, password: String): AccountStatus {
        return firebaseAuthenticationRemoteDataSourceImpl.signUp(email,password)
    }

    override suspend fun signOut() {
        firebaseAuthenticationRemoteDataSourceImpl.signOut()
    }
}