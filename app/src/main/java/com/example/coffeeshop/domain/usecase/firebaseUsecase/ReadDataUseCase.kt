package com.example.coffeeshop.domain.usecase.firebaseUsecase

import com.example.coffeeshop.data.datasource.firebase.FirebaseRepositoryImpl
import com.example.coffeeshop.ui.screen.homepage.FirebaseResponse
import javax.inject.Inject

class ReadDataUseCase @Inject constructor(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun readData(): FirebaseResponse = firebaseRepositoryImpl.readDataFromFirebase()
}