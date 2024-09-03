package com.example.coffeeshop.domain.repository

interface SharedPreferencesRepository {
    suspend fun saveUsername(username: String)
    suspend fun getUsername(): String?
}