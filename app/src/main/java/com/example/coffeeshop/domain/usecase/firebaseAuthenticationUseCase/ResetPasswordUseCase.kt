package com.example.coffeeshop.domain.usecase.firebaseAuthenticationUseCase

import com.example.coffeeshop.data.datasource.firebase.FirebaseRepositoryImpl
import com.example.coffeeshop.ui.screen.settingspage.PasswordChangement
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun resetPassword(email: String): PasswordChangement = firebaseRepositoryImpl.resetPassword(email)
}