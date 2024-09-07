package com.example.coffeeshop.data.datasource.sharedpreference

import com.example.coffeeshop.domain.repository.SharedPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferencesManagerImpl: SharedPreferencesManagerImpl,
    private val coroutineDispatcherIO: CoroutineDispatcher = Dispatchers.IO
): SharedPreferencesRepository {
    override suspend fun saveUsername(username: String?) = withContext(coroutineDispatcherIO){
        sharedPreferencesManagerImpl.saveUsername(username)
    }


    override suspend fun getUsername(): String? = withContext(coroutineDispatcherIO){
        return@withContext sharedPreferencesManagerImpl.getUsername()
    }
}