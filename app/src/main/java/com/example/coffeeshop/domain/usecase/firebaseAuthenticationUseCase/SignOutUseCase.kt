package com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase

import com.example.coffeeshop.domain.repository.FirebaseRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend fun signOut() = firebaseRepository.signOut()
}