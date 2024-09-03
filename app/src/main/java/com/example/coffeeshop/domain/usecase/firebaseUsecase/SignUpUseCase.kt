package com.example.coffeeshop.domain.usecase.firebaseUsecase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.AuthState
import com.example.coffeeshop.ui.screen.signup.AccountStatus
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun signUp(email: String,password: String): AccountStatus = firebaseRepository.signUp(email,password)
}