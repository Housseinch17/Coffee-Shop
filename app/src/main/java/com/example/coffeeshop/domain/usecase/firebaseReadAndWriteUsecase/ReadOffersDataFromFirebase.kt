package com.example.coffeeshop.domain.usecase.firebaseReadAndWriteUsecase

import com.example.coffeeshop.data.datasource.firebase.FirebaseRepositoryImpl
import com.example.coffeeshop.ui.screen.homepage.FirebaseOffersResponse
import javax.inject.Inject

class ReadOffersDataFromFirebase @Inject constructor(
    private val firebaseRepositoryImpl: FirebaseRepositoryImpl
) {
    suspend fun readOffersData(): FirebaseOffersResponse {
        return firebaseRepositoryImpl.readOffersDataFromFirebase()
    }
}