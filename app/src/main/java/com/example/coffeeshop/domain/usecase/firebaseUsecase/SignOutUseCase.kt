package com.example.coffeeshop.domain.usecase.firebaseUsecase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.AuthState
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun signOut(): AuthState = firebaseRepository.signOut()
}