package com.example.coffeeshop.domain.usecase.firebaseUsecase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.AuthState
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun logIn(email: String,password: String): AuthState = firebaseRepository.logIn(email,password)
}