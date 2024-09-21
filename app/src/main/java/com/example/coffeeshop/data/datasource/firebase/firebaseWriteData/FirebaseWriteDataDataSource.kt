package com.example.coffeeshop.data.datasource.firebase.firebaseWriteData

interface FirebaseWriteDataDataSource {
    suspend fun addEmailToUsers(email: String): Boolean
}