package com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun getCurrentUser(): String? = firebaseRepository.getCurrentUser()
}