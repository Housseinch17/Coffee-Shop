package com.example.coffeeshop.data.datasource.firebase.firebaseReadData

import com.example.coffeeshop.ui.screen.homepage.FirebaseResponse

interface FirebaseReadDataDataSource {
    suspend fun readDataFromFirebase(): FirebaseResponse
}