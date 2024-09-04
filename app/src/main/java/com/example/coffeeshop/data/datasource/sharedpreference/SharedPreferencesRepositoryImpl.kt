package com.example.coffeeshop.data.datasource.sharedpreference

import com.example.coffeeshop.domain.repository.SharedPreferencesRepository
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferencesManagerImpl: SharedPreferencesManagerImpl
): SharedPreferencesRepository {
    override suspend fun saveUsername(username: String?) {
        sharedPreferencesManagerImpl.saveUsername(username)
    }


    override suspend fun getUsername(): String? {
        return sharedPreferencesManagerImpl.getUsername()
    }
}