package com.example.coffeeshop.data.datasource.firebase.firebaseReadData

import com.example.coffeeshop.ui.screen.homepage.FirebaseCategoryResponse
import com.example.coffeeshop.ui.screen.homepage.FirebaseOffersResponse

interface FirebaseReadDataDataSource {
    suspend fun readCategoryDataFromFirebase(): FirebaseCategoryResponse
    suspend fun readOffersDataFromFirebase(): FirebaseOffersResponse
}