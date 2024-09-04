package com.example.coffeeshop.domain.usecase.firebaseUsecase

import com.example.coffeeshop.AuthenticationStatus
import com.example.coffeeshop.domain.repository.FirebaseRepository
import com.example.coffeeshop.ui.screen.login.AuthState
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun signOut(): AuthenticationStatus = firebaseRepository.signOut()
}