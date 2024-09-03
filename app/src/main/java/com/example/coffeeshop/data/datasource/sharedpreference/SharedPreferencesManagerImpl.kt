package com.example.coffeeshop.data.datasource.sharedpreference

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPreferencesManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
): SharedPreferencesManager {

    override suspend fun saveUsername(username: String) = withContext(coroutineDispatcher){
        sharedPreferences.edit().putString("username", username).apply()
    }

   override suspend fun getUsername(): String? = withContext(coroutineDispatcher){
        return@withContext sharedPreferences.getString("username", null)
    }
}