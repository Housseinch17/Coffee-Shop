package com.example.coffeeshop.data.datasource.sharedpreference

interface SharedPreferencesManager {
    suspend fun saveUsername(username: String)
    suspend fun getUsername(): String?
}