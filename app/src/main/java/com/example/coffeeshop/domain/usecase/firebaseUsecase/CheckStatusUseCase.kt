package com.example.coffeeshop.domain.usecase.firebaseUsecase

import com.example.coffeeshop.AuthState
import com.example.coffeeshop.domain.repository.FirebaseRepository
import javax.inject.Inject

class CheckStatusUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun checkStatus(): AuthState = firebaseRepository.checkStatus()
}